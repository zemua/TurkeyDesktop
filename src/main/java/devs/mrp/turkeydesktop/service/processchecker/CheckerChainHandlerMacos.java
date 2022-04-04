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
import java.util.Objects;
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
            
            String pid = getOneLinedValueOf(pidQuery);
            String name = getOneLinedValueOf(nameQuery);
            String title = getOneLinedAppleScript(queryForTitle);
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
        if (Objects.nonNull(line)) {
            String[] unparsed = line.split("=");
            if (unparsed.length == 2) {
                String valueInQuotes = unparsed[1];
                if (valueInQuotes.contains("\"")) {
                    valueInQuotes = valueInQuotes.substring(1, valueInQuotes.length() - 1);
                }
                return valueInQuotes;
            }
        }
        return "";
    }

    private String getOneLinedAppleScript(String[] comands) throws IOException, InterruptedException {
        Process p = r.exec(comands);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = b.readLine();
        b.close();
        if (Objects.nonNull(line)) {
            String[] unparsed = line.split("=");
            if (unparsed.length == 2) {
                String valueInQuotes = unparsed[1];
                if (valueInQuotes.contains("\"")) {
                    valueInQuotes = valueInQuotes.substring(1, valueInQuotes.length() - 1);
                }
                return valueInQuotes;
            }
        }
        return "";
    }

    private String getDirectOutput(String query) throws IOException, InterruptedException {
        Process p = r.exec(query);
        p.waitFor();
        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = b.readLine();
        b.close();
        if (Objects.nonNull(line)) {
            return line;
        }
        return "";
    }

}
