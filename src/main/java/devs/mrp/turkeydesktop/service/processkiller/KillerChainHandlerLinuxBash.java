/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processkiller;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class KillerChainHandlerLinuxBash extends ChainHandler<String> {
    
    private Runtime r = Runtime.getRuntime();
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(String pid) {
        try {
            r.exec(String.format("kill -9 %s", pid));
        } catch (IOException ex) {
            Logger.getLogger(KillerChainHandlerLinuxBash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
