/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.group.GroupServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.service.processchecker.ProcessCheckerFactory;
import devs.mrp.turkeydesktop.service.processkiller.KillerChainCommander;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLogger;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritter;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritterFactory;
import devs.mrp.turkeydesktop.service.processchecker.ProcessChecker;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainBaseHandler;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainFactory;
import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author miguel
 */
public class WatchDogImpl implements WatchDog {
    
    private List<FeedbackListener<String,TimeLog>> listeners = new ArrayList<>();
    
    private static final Logger LOGGER = Logger.getLogger(WatchDogImpl.class.getName());

    private static final long SLEEP_MILIS = 3000;
    private static final Semaphore semaphore = new Semaphore(1);

    private static WatchDog instance;
    
    private DbLogger dbLogger;

    private boolean on = true;
    private JTextArea mLogger;
    private SwingWorker<Object, Object> worker;
    private AtomicLong timestamp;
    private ProcessChecker processChecker;
    private LocaleMessages localeMessages;
    private ConditionChecker conditionChecker;
    private ChainHandler<String> killerHandler = new KillerChainCommander().getHandlerChain();
    private Logger logger = Logger.getLogger(WatchDogImpl.class.getName());
    private ExportWritter exportWritter = ExportWritterFactory.getWritter();
    private TrayChainBaseHandler trayHandler = TrayChainFactory.getChain();
    private ResourceHandler<Image,ImagesEnum> imageHandler = ResourceHandlerFactory.getImagesHandler();
    private GroupService groupService = GroupServiceFactory.getService();

    private WatchDogImpl() {
        initConditionChecker();
        timestamp = new AtomicLong();
        processChecker = ProcessCheckerFactory.getNew();
        localeMessages = LocaleMessages.getInstance();
        dbLogger = DbLoggerFactory.getNew();
    }
    
    private void initConditionChecker() {
        try {
            conditionChecker = ConditionCheckerFactory.getConditionChecker();
        } catch (Exception e) {
            stop();
        }
    }

    public static WatchDog getInstance() {
        if (instance == null) {
            instance = new WatchDogImpl();
        }
        return instance;
    }
    
    @Override
    public void begin(JTextArea logger) {
        setLogger(logger);
        begin();
    }
    
    public void begin() {
        if (semaphore.availablePermits() < 1) {
            // if it is already running, don't duplicate it
            return;
        }
        try {
            semaphore.acquire();
            // if it is not running, set it up and execute it
            if (on && (worker == null || worker.isDone() || worker.getState().equals(SwingWorker.StateValue.PENDING))) {
                initializeWorker();
                timestamp.set(System.currentTimeMillis());
                worker.execute();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    @Override
    public void setLogger(JTextArea logger) {
        mLogger = logger;
    }

    @Override
    public void stop() {
        on = false;
    }

    private void log(String text) {
        if (mLogger != null) {
            mLogger.append(String.format("%s \n", text));
        }
        LOGGER.log(Level.INFO, text);
    }

    private void initializeWorker() {
        this.worker = new SwingWorker<>() {
            @Override
            protected Object doInBackground() throws Exception {
                while (WatchDogImpl.this.on && Thread.currentThread().isAlive()) {
                    Thread.sleep(SLEEP_MILIS);
                    try {
                        doLoopedStuff();
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Exception while running watchdog", e);
                    }
                    // publish calls to process() method of SwingWorker
                    //publish();
                }
                return null;
            }

            @Override
            protected void process(List<Object> chunks) {
                //doLoopedStuff();
            }
        };
    }

    private void doLoopedStuff() {
        Long current = System.currentTimeMillis();
        processChecker.refresh();
        Long elapsed = current - timestamp.getAndSet(current);
        
        // insert entry to db
        TimeLog entry = dbLogger.logEntry(elapsed, processChecker.currentProcessPid(), processChecker.currentProcessName(), processChecker.currentWindowTitle());
        
        boolean conditionsMet = conditionChecker.areConditionsMet(entry.getGroupId());
        boolean isLockDown = conditionChecker.isLockDownTime();
        conditionChecker.notifyCloseToConditionsRefresh();
        if (entry.isBlockable() && (conditionChecker.timeRemaining() <= 0 || !conditionsMet || isLockDown)) {
            killerHandler.receiveRequest(null, processChecker.currentProcessPid());
            Toaster.sendToast(localeMessages.getString("killingProcess"));
        }
        
        if (!conditionsMet) {
            Toaster.sendToast(localeMessages.getString("conditionsNotMetFor") + " " + groupService.findById(entry.getGroupId()).getName());
        }
        
        if (entry.getCounted() < 0 && conditionChecker.isTimeRunningOut()) {
            Toaster.sendToast(localeMessages.getString("timeRunningOut"));
        }
        
        try {
            FileHandler.exportAccumulated(entry.getAccumulated());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting accumulated time to file", e);
        }
        
        exportWritter.exportChanged();
        
        giveFeedback("Entry logged", entry);
        
        updateTrayIcon(isLockDown, entry.getCounted());
        trayHandler.requestChangeTimeLeft("time", conditionChecker.timeRemaining());
    }

    @Override
    public void addFeedbacker(FeedbackListener<String, TimeLog> feedbackListener) {
        listeners.add(feedbackListener);
    }
    
    private void giveFeedback(String message, TimeLog entryData) {
        listeners.forEach(l -> l.giveFeedback(message, entryData));
    }

    @Override
    public void removeFeedbacker(FeedbackListener<String, TimeLog> feedbackListener) {
        listeners.remove(feedbackListener);
    }
    
    private void updateTrayIcon(boolean lockdown, long counted) {
        if (lockdown) {
            trayHandler.requestChangeIcon("icon", imageHandler.getResource(ImagesEnum.BADGE));
            return;
        }
        if (counted == 0) {
            trayHandler.requestChangeIcon("icon", imageHandler.getResource(ImagesEnum.TURKEY));
            return;
        }  
        if (counted > 0) {
            trayHandler.requestChangeIcon("icon", imageHandler.getResource(ImagesEnum.FIRE));
            return;
        }
        trayHandler.requestChangeIcon("icon", imageHandler.getResource(ImagesEnum.SNOW));
    }

}
