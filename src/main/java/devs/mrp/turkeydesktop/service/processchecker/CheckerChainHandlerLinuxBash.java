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
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(IProcessInfo data) {
        try {
            Runtime r = Runtime.getRuntime();
            Process pExport = r.exec("export DISPLAY=\":1\"");
            Process p = r.exec("uname -a");
            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            
            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }
            
            b.close();
        } catch (IOException ex) {
            Logger.getLogger(CheckerChainHandlerLinuxBash.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(CheckerChainHandlerLinuxBash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
