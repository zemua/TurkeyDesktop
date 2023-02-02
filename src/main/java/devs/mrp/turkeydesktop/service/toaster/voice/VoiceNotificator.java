/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.toaster.voice;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.HashMap;
import java.util.Map;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;

/**
 *
 * @author ncm55070
 */
public class VoiceNotificator {
    private static final ChainHandler<String> speaker = new VoiceNotificatorChainCommander().getHandlerChain();
    private static final Map<String,Long> messagesTimestamp = new HashMap<>();
    private static final long sleep = 1000*59; // 1 minute in milliseconds between toasts of same message
    private static final ConfigElementService config = ConfigElementFactoryImpl.getService();
    
    public static void speakMessage(String msg) {
        long now = System.currentTimeMillis();
        if (messagesTimestamp.containsKey(msg) && messagesTimestamp.get(msg) > now - sleep) {
            return;
        }
        config.findById(ConfigurationEnum.SPEAK).subscribe(c -> {
            if (!"true".equals(c.getValue())) {
                return;
            }
            messagesTimestamp.put(msg, now);
            speaker.receiveRequest(null, msg);
        });
        
    }
}
