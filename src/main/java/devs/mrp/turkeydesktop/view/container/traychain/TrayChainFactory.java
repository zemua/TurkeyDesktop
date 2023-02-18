package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;

public interface TrayChainFactory {
    
    TrayChainBaseHandler getChain();
    WatchDog getWatchDog();
    TimeConverter getTimeConverter();
    TrayChainBaseHandler getLinuxHandler();
    TrayChainBaseHandler getMacHandler();
    
}
