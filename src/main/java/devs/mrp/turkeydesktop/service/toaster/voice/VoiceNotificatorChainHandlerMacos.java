/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.toaster.voice;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.service.toaster.ToasterChainHandlerMacos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ncm55070
 */
public class VoiceNotificatorChainHandlerMacos extends ChainHandler<String> {

    private Runtime r = Runtime.getRuntime();
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isMac();
    }

    @Override
    protected void handle(String data) {
        String message = String.format("say \"%s\"", data);
        String[] sender = {"osascript", "-e", message};
        try {
            r.exec(sender);
        } catch (IOException ex) {
            Logger.getLogger(ToasterChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
