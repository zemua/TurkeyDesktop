package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.service.toaster.Toaster;

public interface ConditionCheckerFactory {
    ConditionChecker getConditionChecker();
    ConfigElementService getConfigElementService();
    Toaster getToaster();
}
