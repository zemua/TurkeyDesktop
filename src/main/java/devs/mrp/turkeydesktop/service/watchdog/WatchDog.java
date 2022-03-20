/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.service.processchecker.FProcessChecker;
import devs.mrp.turkeydesktop.service.processchecker.IProcessChecker;
import devs.mrp.turkeydesktop.service.processkiller.KillerChainCommander;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLogger;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLoggerF;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;

/**
 *
 * @author miguel
 */
public class WatchDog implements IWatchDog {
    
    // TODO start as daemon in system tray, open window on click, and hide on close
    // TODO export time of groups to txt for the last x number of days
    // TODO adding conditions for titles, remove case sensitive
    // TODO adding conditions for titles, highlight which ones are positive or negative
    // TODO error when adding condition to group for more than 0 days
    // TODO after adding condition the list of groups to select gets repeated
    
    private static final Logger LOGGER = Logger.getLogger(WatchDog.class.getName());

    private static final long SLEEP_MILIS = 3000;
    private static final Semaphore semaphore = new Semaphore(1);

    private static IWatchDog instance;
    
    private DbLogger dbLogger;

    private boolean on = true;
    private JTextArea mLogger;
    private SwingWorker<Object, Object> worker;
    private AtomicLong timestamp;
    private IProcessChecker processChecker;
    private LocaleMessages localeMessages;
    private final ConditionChecker conditionChecker = ConditionCheckerFactory.getConditionChecker();
    private ChainHandler<String> killerHandler = new KillerChainCommander().getHandlerChain();
    private Logger logger = Logger.getLogger(WatchDog.class.getName());

    private WatchDog() {
        timestamp = new AtomicLong();
        processChecker = FProcessChecker.getNew();
        localeMessages = LocaleMessages.getInstance();
        dbLogger = DbLoggerF.getNew();
    }

    public static IWatchDog getInstance() {
        if (instance == null) {
            instance = new WatchDog();
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
            if (worker == null || worker.isDone() || worker.getState().equals(SwingWorker.StateValue.PENDING)) {
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
                while (WatchDog.this.on && Thread.currentThread().isAlive()) {
                    Thread.sleep(SLEEP_MILIS);
                    doLoopedStuff();
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
        if (entry.isBlockable() && (entry.getAccumulated() <= 0 || !conditionsMet)) {
            killerHandler.receiveRequest(null, processChecker.currentProcessPid());
            Toaster.sendToast(localeMessages.getString("killingProcess"));
        }
        
        if (!conditionsMet) {
            Toaster.sendToast(localeMessages.getString("conditionsNotMet"));
        }
        
        if (entry.getCounted() < 0 && conditionChecker.isTimeRunningOut()) {
            Toaster.sendToast(localeMessages.getString("timeRunningOut"));
        }
        
        try {
            FileHandler.exportAccumulated(entry.getAccumulated());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting accumulated time to file", e);
        }
        
    }

}
