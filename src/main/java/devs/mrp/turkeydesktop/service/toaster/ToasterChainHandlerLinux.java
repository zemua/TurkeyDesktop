/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.toaster;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ToasterChainHandlerLinux extends ChainHandler<String> {
    
    private Runtime r = Runtime.getRuntime();
    private String title = "Time Turkey Desktop";

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(String data) {
        try {
            String[] cmd = {"notify-send", "-t", "10000", title, data};
            r.exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(ToasterChainHandlerLinux.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
