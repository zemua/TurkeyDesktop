package devs.mrp.turkeydesktop.common.factory;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.toaster.voice.VoiceNotificator;

public class CommonBeans {
    
    private static TimeConverter timeConverter;
    private static Toaster toaster;
    private static VoiceNotificator voiceNotificator;
    private static FileHandler fileHandler;
    
    public static TimeConverter getTimeConverter() {
        if (timeConverter == null) {
            timeConverter = new TimeConverter(ConfigElementFactoryImpl.getInstance().getService());
        }
        return timeConverter;
    }
    
    public static VoiceNotificator getVoiceNotificator() {
        if (voiceNotificator == null) {
            voiceNotificator = VoiceNotificator.getInstance(ConfigElementFactoryImpl.getInstance().getService());
        }
        return voiceNotificator;
    }
    
    public static Toaster getToaster() {
        if (toaster == null) {
            toaster = Toaster.getInstance(getVoiceNotificator());
        }
        return toaster;
    }
    
    public static FileHandler getFileHandler() {
        if (fileHandler == null) {
            fileHandler = new FileHandler(ConfigElementFactoryImpl.getInstance().getService());
        }
        return fileHandler;
    }
    
}
