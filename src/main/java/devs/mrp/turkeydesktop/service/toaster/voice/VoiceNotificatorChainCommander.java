/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.toaster.voice;

import devs.mrp.turkeydesktop.common.ChainCommander;
import devs.mrp.turkeydesktop.common.ChainHandler;

/**
 *
 * @author ncm55070
 */
public class VoiceNotificatorChainCommander implements ChainCommander {

    private ChainHandler<String> linuxHandler;
    private ChainHandler<String> macosHandler;
    
    @Override
    public ChainHandler getHandlerChain() {
        linuxHandler = new VoiceNotificatorChainHandlerLinux();
        macosHandler = new VoiceNotificatorChainHandlerMacos();
        
        linuxHandler.setNextHandler(macosHandler);
        
        return linuxHandler;
    }
    
}
