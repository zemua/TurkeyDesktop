package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritter;
import devs.mrp.turkeydesktop.service.processchecker.ProcessChecker;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLogger;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainBaseHandler;
import io.reactivex.rxjava3.core.Single;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JTextArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WatchDogImpl implements WatchDog {
    
    private List<FeedbackListener<String,TimeLog>> listeners = new ArrayList<>();

    private static final long SLEEP_MILIS = 3000;
    private static final Semaphore semaphore = new Semaphore(1);
    
    private final DbLogger dbLogger;
    private boolean on = true;
    private JTextArea mLogger;
    private final AtomicLong timestamp;
    private final ProcessChecker processChecker;
    private final LocaleMessages localeMessages;
    private final ConditionChecker conditionChecker;
    private final ChainHandler<String> killerHandler;
    private final ExportWritter exportWritter;
    private final TrayChainBaseHandler trayHandler;
    private final ResourceHandler<Image,ImagesEnum> imageHandler;
    private final GroupService groupService;
    private final Toaster toaster;
    private final FileHandler fileHandler;
    
    private ExecutorService loopedExecutor = WorkerFactory.getSingleThreadExecutor();
    Future<?> loopFuture = null;

    public WatchDogImpl(WatchDogFactory factory) {
        this.conditionChecker = factory.getConditionChecker();
        this.timestamp = new AtomicLong();
        this.processChecker = factory.getProcessChecker();
        this.localeMessages = LocaleMessages.getInstance();
        this.dbLogger = factory.getDbLogger();
        this.toaster = factory.getToaster();
        this.fileHandler = factory.getFileHandler();
        this.exportWritter = factory.getExportWritter();
        this.groupService = factory.getGroupService();
        this.trayHandler = factory.getTrayChainBaseHandler();
        this.killerHandler = factory.getKillerHandler();
        this.imageHandler = factory.getImageHandler();
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
            if (on && (loopFuture == null || loopFuture.isCancelled() || loopFuture.isDone() || loopedExecutor.isShutdown() || loopedExecutor.isTerminated())) {
                timestamp.set(System.currentTimeMillis());
                Thread watchdogThread = Thread.currentThread();
                loopFuture = loopedExecutor.submit(() -> {
                    while (WatchDogImpl.this.on && watchdogThread.isAlive()) {
                        try {
                            Thread.sleep(SLEEP_MILIS);
                            try {
                                doLoopedStuff();
                            } catch (Exception e) {
                                log.error("Exception while running watchdog loop", e);
                            }
                        } catch (InterruptedException ex) {
                            log.error("Interrupted Exception on watchdog", ex);
                        }
                    }
                });
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
        log.info(text);
    }

    private void doLoopedStuff() {
        Long current = System.currentTimeMillis();
        processChecker.refresh();
        Long elapsed = current - timestamp.getAndSet(current);
        
        // insert entry to db
        dbLogger.logEntry(elapsed, processChecker.currentProcessPid(), processChecker.currentProcessName(), processChecker.currentWindowTitle()).subscribe(entry -> {
            Single<Boolean> condsMet = conditionChecker.areConditionsMet(entry.getGroupId());
            Single<Boolean> lockDown = conditionChecker.isLockDownTime();
            Single<Long> remain = conditionChecker.timeRemaining();
            conditionChecker.notifyCloseToConditionsRefresh().subscribe();
            
            Single.zip(condsMet, lockDown, remain, (conditionsMet, isLockDown, remaining) -> {
                log.debug("Conditions Met: {}", conditionsMet);
                log.debug("Lock Down: {}", isLockDown);
                if (entry.isBlockable() && (remaining <= 0 || !conditionsMet || isLockDown)) {
                    killerHandler.receiveRequest(null, processChecker.currentProcessPid());
                    toaster.sendToast(localeMessages.getString("killingProcess"));
                }
                if (!conditionsMet) {
                    groupService.findById(entry.getGroupId()).subscribe(groupResult -> {
                        toaster.sendToast(localeMessages.getString("conditionsNotMetFor") + " " + groupResult.getName());
                    });
                }
                conditionChecker.isTimeRunningOut().subscribe(isRunningOut -> {
                    if (entry.getCounted() < 0 && isRunningOut) {
                        toaster.sendToast(localeMessages.getString("timeRunningOut"));
                    }
                });
                try {
                    fileHandler.exportAccumulated(entry.getAccumulated());
                } catch (IOException e) {
                    log.error("Error exporting accumulated time to file", e);
                }
                exportWritter.exportChanged();

                giveFeedback("Entry logged", entry);

                updateTrayIcon(isLockDown, entry.getCounted());
                trayHandler.requestChangeTimeLeft("time", remaining);
                return Single.just(true);
            }).subscribe();
        });
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
