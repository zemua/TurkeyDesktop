/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.processkiller;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ncm55070
 */
public class KillerChainHandlerMacos extends ChainHandler<String> {
    
    private Runtime r = Runtime.getRuntime();

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isMac();
    }

    @Override
    protected void handle(String data) {
        try {
            r.exec(String.format("kill -9 %s", data));
        } catch (IOException ex) {
            Logger.getLogger(KillerChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
