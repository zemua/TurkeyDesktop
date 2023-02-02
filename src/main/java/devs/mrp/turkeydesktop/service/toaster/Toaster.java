/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.toaster;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.service.toaster.voice.VoiceNotificator;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.HashMap;
import java.util.Map;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;

/**
 *
 * @author miguel
 */
public class Toaster {
    
    private static final ChainHandler<String> toaster = new ToasterChainCommander().getHandlerChain();
    private static final Map<String,Long> messagesTimestamp = new HashMap<>();
    private static final long sleep = 1000*60; // 1 minute in milliseconds between toasts of same message
    
    public static void sendToast(String msg) {
        long now = System.currentTimeMillis();
        if (messagesTimestamp.containsKey(msg) && messagesTimestamp.get(msg) > now - sleep) {
            return;
        }
        messagesTimestamp.put(msg, now);
        toaster.receiveRequest(null, msg);
        VoiceNotificator.speakMessage(msg);
    }
    
}
