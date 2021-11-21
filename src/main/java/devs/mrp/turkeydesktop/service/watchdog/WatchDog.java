/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.processchecker.FProcessChecker;
import devs.mrp.turkeydesktop.service.processchecker.IProcessChecker;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author miguel
 */
public class WatchDog implements IWatchDog {

    private static final long SLEEP_MILIS = 3000;
    private static final Semaphore semaphore = new Semaphore(1);

    private static IWatchDog instance;

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
    }

    public static IWatchDog getInstance() {
        if (instance == null) {
            instance = new WatchDog();
        }
        return instance;
    }

    /**
     * Not thread-safe
     *
     * @param logger
     */
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
        }
        semaphore.release();
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
    }

    private void initializeWorker() {
        this.worker = new SwingWorker<>() {
            @Override
            protected Object doInBackground() throws Exception {
                while (WatchDog.this.on) {
                    Thread.sleep(SLEEP_MILIS);
                    // publish calls to process method of SwingWorker
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
        Long elapsed = current - timestamp.getAndSet(current);
        log(String.format(localeMessages.getString("elapsedmillis"), elapsed));
        processChecker.refresh();
        log(String.format(localeMessages.getString("windowname"), processChecker.currentWindowTitle()));
        log(String.format(localeMessages.getString("currentpid"), processChecker.currentProcessPid()));
        log(String.format(localeMessages.getString("currentprocess"), processChecker.currentProcessName()));
    }

}
