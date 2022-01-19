/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.processchecker.FProcessChecker;
import devs.mrp.turkeydesktop.service.processchecker.IProcessChecker;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLogger;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLoggerF;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author miguel
 */
public class WatchDog implements IWatchDog {
    
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
        try {
            semaphore.acquire();
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
                while (WatchDog.this.on) {
                    Thread.sleep(SLEEP_MILIS);
                    // publish calls to process() method of SwingWorker
                    publish();
                }
                return null;
            }

            @Override
            protected void process(List<Object> chunks) {
                doLoopedStuff();
            }
        };
    }

    private void doLoopedStuff() {
        Long current = System.currentTimeMillis();
        processChecker.refresh();
        Long elapsed = current - timestamp.getAndSet(current);
        
        // log some stuff
        log(String.format(localeMessages.getString("elapsedmillis"), elapsed));
        log(String.format(localeMessages.getString("windowname"), processChecker.currentWindowTitle()));
        log(String.format(localeMessages.getString("currentpid"), processChecker.currentProcessPid()));
        log(String.format(localeMessages.getString("currentprocess"), processChecker.currentProcessName()));
        
        // insert entry to db
        dbLogger.logEntry(elapsed, processChecker.currentProcessPid(), processChecker.currentProcessName(), processChecker.currentWindowTitle());
    }

}
