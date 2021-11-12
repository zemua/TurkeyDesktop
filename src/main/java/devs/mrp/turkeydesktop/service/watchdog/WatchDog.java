/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.service.processchecker.FProcessChecker;
import devs.mrp.turkeydesktop.service.processchecker.IProcessChecker;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author miguel
 */
public class WatchDog implements IWatchDog {

    private static final long SLEEP_MILIS = 3000;
    
    private static IWatchDog instance;

    private boolean on = true;
    private JTextArea mLogger;
    private SwingWorker worker;
    private AtomicLong timestamp;
    private IProcessChecker processChecker;

    private WatchDog() {
        timestamp = new AtomicLong();
        processChecker = FProcessChecker.getNew();
    }
    
    public static IWatchDog getInstance() {
        if (instance == null) {
            instance = new WatchDog();
        }
        return instance;
    }

    /**
     * Not thread-safe
     * @param logger 
     */
    @Override
    public void begin(JTextArea logger) {
        setLogger(logger);
        if (worker == null || worker.isDone() || worker.getState().equals(SwingWorker.StateValue.PENDING)) {
            initializeWorker();
            timestamp.set(System.currentTimeMillis());
            worker.execute();
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
        mLogger.append(String.format("%s \n", text));
    }

    private void initializeWorker() {
        this.worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                while (WatchDog.this.on) {
                    Thread.sleep(SLEEP_MILIS);
                    publish();
                }
                return null;
            }

            @Override
            protected void process(List chunks) {
                doLoopedStuff();
            }
        };
    }

    private void doLoopedStuff() {
        Long current = System.currentTimeMillis();
        Long elapsed = current - timestamp.getAndSet(current);
        log(String.format("elapsed %d millis", elapsed));
        log(String.format("current window name: %s", processChecker.currentWindowTitle()));
        log(String.format("current process PID: %s", processChecker.currentProcessPid()));
        log(String.format("current process name: %s", processChecker.currentProcessName()));
    }

}
