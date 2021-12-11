/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.database.type.Type;

/**
 *
 * @author miguel
 */
public class LogAndTypeFacade {
    
    private long id;
    private long epoch;
    private long elapsed;
    private long counted;
    private String pid;
    private String processName;
    private String windowTitle;
    private Type.Types type;

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

    public Type.Types getType() {
        return type;
    }

    public void setType(Type.Types type) {
        this.type = type;
    }
    
}
