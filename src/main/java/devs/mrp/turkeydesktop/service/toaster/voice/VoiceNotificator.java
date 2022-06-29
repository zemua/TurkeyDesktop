/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.toaster.voice;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ncm55070
 */
public class VoiceNotificator {
    private static final ChainHandler<String> speaker = new VoiceNotificatorChainCommander().getHandlerChain();
    private static final Map<String,Long> messagesTimestamp = new HashMap<>();
    private static final long sleep = 1000*60; // 1 minute in milliseconds between toasts of same message
    private static final IConfigElementService config = FConfigElementService.getService();
    
    public static void speakMessage(String msg) {
        long now = System.currentTimeMillis();
        if (messagesTimestamp.containsKey(msg) && messagesTimestamp.get(msg) > now - sleep) {
            return;
        }
        if (!config.findById(ConfigurationEnum.SPEAK).getValue().equals("true")) {
            return;
        }
        messagesTimestamp.put(msg, now);
        speaker.receiveRequest(null, msg);
    }
}
