package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDog;

public interface StarterFactory {
    
    Starter getStarterController();
    WatchDog getWatchDog();
    
}
