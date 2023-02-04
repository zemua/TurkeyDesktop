package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.config.ConfigElementService;

public interface ConditionCheckerFactory {
    ConditionChecker getConditionChecker();
    ConfigElementService getConfigElementService();
}
