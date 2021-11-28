/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;

/**
 *
 * @author miguel
 */
public class TimeLog {
    
    public static final String ID = "ID";
    public static final String EPOCH = "EPOCH";
    public static final String ELAPSED = "ELAPSED";
    public static final String PID = "PID";
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String WINDOW_TITLE = "WINDOW_TITLE";
    
    private long id;
    private long epoch;
    private long elapsed;
    private String pid;
    private String processName;
    private String windowTitle;
    
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
    
    @Override
    public String toString() {
        return String.format(String.format(localeMessages.getString("timeLogToString"), epoch, processName, windowTitle, elapsed));
    }
    
}
