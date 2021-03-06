/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.processchecker;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ncm55070
 */
public class CheckerChainHandlerMacos extends ChainHandler<ProcessInfo> {

    private Runtime r = Runtime.getRuntime();

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isMac();
    }

    @Override
    protected void handle(ProcessInfo data) {
        try {
            String frontAppQuery = "lsappinfo front";
            String frontApp = getDirectOutput(frontAppQuery);

            String pidQuery = "lsappinfo info -only pid ".concat(frontApp);
            String nameQuery = "lsappinfo info -only name ".concat(frontApp);
            
            String[] queryForTitle = {"osascript", "-e", "tell application \"System Events\" to tell (process 1 whose it is frontmost) ¬\n"
                + "        to tell (window 1 whose value of attribute \"AXMain\" is true) ¬\n"
                + "        to set windowTitle to value of attribute \"AXTitle\""};

            data.setProcessName(getOneLinedValueOf(nameQuery));
            data.setProcessPid(getOneLinedValueOf(pidQuery));
            data.setWindowTitle(getOneLinedAppleScript(queryForTitle));
        } catch (IOException ex) {
            Logger.getLogger(CheckerChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CheckerChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getOneLinedValueOf(String query) throws IOException, InterruptedException {
        Process p = r.exec(query);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = b.readLine();
        b.close();
        return Optional.ofNullable(line)
                .map(l -> line.split("="))
                .filter(arr -> arr.length == 2)
                .map(arr -> arr[1])
                .map(str -> str.replace("\"", ""))
                .orElse("");
    }

    private String getOneLinedAppleScript(String[] comands) throws IOException, InterruptedException {
        Process p = r.exec(comands);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = Optional.ofNullable(b.readLine()).orElse("");
        b.close();
        return line;
    }

    private String getDirectOutput(String query) throws IOException, InterruptedException {
        Process p = r.exec(query);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = Optional.ofNullable(b.readLine()).orElse("");
        b.close();
        return line;
    }

}
