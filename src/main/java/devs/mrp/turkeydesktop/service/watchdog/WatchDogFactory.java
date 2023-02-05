package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.toaster.Toaster;

public interface WatchDogFactory {
    
    WatchDog getInstance();
    ConditionChecker getConditionChecker();
    Toaster getToaster();
    
}
