/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class CheckerChainHandlerLinuxBash extends ChainHandler<IProcessInfo> {

    private static final String EXPORT_DISPLAY_COMMAND = "bash export DISPLAY=:1";
    private static final String WINDOW_TITLE_COMMAND = "xdotool getwindowfocus getwindowname";
    private static final String PROCESS_PID_COMMAND = "xdotool getactivewindow getwindowpid";

    Runtime r = Runtime.getRuntime();

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(IProcessInfo data) {
        try {
            exportDisplay();
            data.setWindowTitle(commandOutput(WINDOW_TITLE_COMMAND));
            data.setProcessPid(commandOutput(PROCESS_PID_COMMAND));
            data.setProcessName(commandOutput(String.format("ps -p %s -o comm=", data.getProcessPid())));
        } catch (IOException ex) {
            Logger.getLogger(CheckerChainHandlerLinuxBash.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CheckerChainHandlerLinuxBash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exportDisplay() throws IOException, InterruptedException {
        Process pExport = r.exec(EXPORT_DISPLAY_COMMAND);
        pExport.waitFor();
    }

    private String commandOutput(String command) throws IOException, InterruptedException {
        Process p = r.exec(command);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        StringBuilder output = new StringBuilder();
        while ((line = b.readLine()) != null) {
            output.append(line);
        }
        b.close();
        return output.toString();
    }

}
