package devs.mrp.turkeydesktop.service.processchecker;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckerChainHandlerMacos extends ChainHandler<ProcessInfo> {

    private Runtime r = Runtime.getRuntime();

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isMac();
    }

    @Override
    protected void handle(ProcessInfo data) {
        try {
            data.setProcessName(getOneLinedAppleScript(queryForProcess("name")));
            data.setProcessPid(getOneLinedAppleScript(queryForProcess("unix id")));
            data.setWindowTitle(getOneLinedAppleScript(queryForWindowTitle()));
        } catch (IOException ex) {
            Logger.getLogger(CheckerChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CheckerChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String[] queryForProcess(String property) {
        String[] queryForProcess = {"osascript", "-e", 
            "tell application \"System Events\" to tell (first process whose frontmost is true) to return {" + property + "}"};
        return queryForProcess;
    }
    
    private String[] queryForWindowTitle() {
        String[] queryForTitle = {"osascript", "-e", "tell application \"System Events\" to tell (process 1 whose it is frontmost) ¬\n"
                + "        to tell (window 1 whose value of attribute \"AXMain\" is true) ¬\n"
                + "        to set windowTitle to value of attribute \"AXTitle\""};
        return queryForTitle;
    }

    private String getOneLinedAppleScript(String[] comands) throws IOException, InterruptedException {
        Process p = r.exec(comands);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = Optional.ofNullable(b.readLine()).orElse("");
        b.close();
        return line;
    }

}
