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
    public static final String COUNTED = "COUNTED";
    public static final String ACCUMULATED = "ACCUMULATED";
    public static final String PID = "PID";
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String WINDOW_TITLE = "WINDOW_TITLE";
    
    private long id;
    private long epoch;
    private long elapsed;
    private long counted;
    private long accumulated;

    public long getAccumulated() {
        return accumulated;
    }

    public void setAccumulated(long accumulated) {
        this.accumulated = accumulated;
    }
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

    public long getCounted() {
        return counted;
    }

    public void setCounted(long counted) {
        this.counted = counted;
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
        return String.format(String.format(localeMessages.getString("timeLogToString"), epoch, elapsed, processName, windowTitle));
    }
    
}
