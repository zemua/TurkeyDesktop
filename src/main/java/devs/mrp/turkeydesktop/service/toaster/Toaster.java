package devs.mrp.turkeydesktop.service.toaster;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.service.toaster.voice.VoiceNotificator;
import java.util.HashMap;
import java.util.Map;

public class Toaster {
    
    private static final long sleep = 1000*60; // 1 minute in milliseconds between toasts of same message
    
    private final ChainHandler<String> toaster = new ToasterChainCommander().getHandlerChain();
    private final Map<String,Long> messagesTimestamp = new HashMap<>();
    private final VoiceNotificator voiceNotificator;
    
    private static Toaster instance;
    
    private Toaster(VoiceNotificator voiceNotificator) {
        this.voiceNotificator = voiceNotificator;
    }
    
    public static Toaster getInstance(VoiceNotificator voiceNotificator) {
        if (instance == null) {
            instance = new Toaster(voiceNotificator);
        }
        return instance;
    }
    
    public void sendToast(String msg) {
        long now = System.currentTimeMillis();
        if (messagesTimestamp.containsKey(msg) && messagesTimestamp.get(msg) > now - sleep) {
            return;
        }
        messagesTimestamp.put(msg, now);
        toaster.receiveRequest(null, msg);
        voiceNotificator.speakMessage(msg);
    }
    
}
