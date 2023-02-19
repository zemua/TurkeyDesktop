package devs.mrp.turkeydesktop.service.processchecker;

public interface ProcessInfo {

    public String getWindowTitle();

    public void setWindowTitle(String windowTitle);

    public String getProcessName();

    public void setProcessName(String processName);

    public String getProcessPid();

    public void setProcessPid(String processPid);
}
