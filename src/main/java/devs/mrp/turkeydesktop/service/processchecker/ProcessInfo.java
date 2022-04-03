/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

/**
 *
 * @author miguel
 */
public interface ProcessInfo {

    public String getWindowTitle();

    public void setWindowTitle(String windowTitle);

    public String getProcessName();

    public void setProcessName(String processName);

    public String getProcessPid();

    public void setProcessPid(String processPid);
}
