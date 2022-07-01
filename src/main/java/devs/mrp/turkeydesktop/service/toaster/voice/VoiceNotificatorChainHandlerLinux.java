/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.toaster.voice;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zemua
 */
public class VoiceNotificatorChainHandlerLinux extends ChainHandler<String> {

    private Runtime r = Runtime.getRuntime();
    private LocaleMessages locale = LocaleMessages.getInstance();
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(String data) {
        String lang = locale.language();
        try {
            String[] cmd = {"spd-say", "-l", locale.language(), data};
            r.exec(cmd);
        } catch (IOException ex) {
            Logger.getLogger(VoiceNotificatorChainHandlerLinux.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
