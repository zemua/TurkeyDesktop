package devs.mrp.turkeydesktop.service.processchecker;

public class ProcessInfoImpl implements ProcessInfo {
    
    private String windowTitle;
    private String processName;
    private String processPid;

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessPid() {
        return processPid;
    }

    public void setProcessPid(String processPid) {
        this.processPid = processPid;
    }
}
