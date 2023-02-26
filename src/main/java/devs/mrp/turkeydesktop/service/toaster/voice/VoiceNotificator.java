package devs.mrp.turkeydesktop.service.toaster.voice;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.HashMap;
import java.util.Map;

public class VoiceNotificator {
    
    private static final long sleep = 1000*59; // 1 minute in milliseconds between toasts of same message
    
    private final ChainHandler<String> speaker = new VoiceNotificatorChainCommander().getHandlerChain();
    private final Map<String,Long> messagesTimestamp = new HashMap<>();
    private final ConfigElementService config;
    
    private static VoiceNotificator instance;
    
    private VoiceNotificator(ConfigElementService config) {
        this.config = config;
    }
    
    public static VoiceNotificator getInstance(ConfigElementService config) {
        if (instance == null) {
            instance = new VoiceNotificator(config);
        }
        return instance;
    }
    
    public void speakMessage(String msg) {
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
