package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class ConditionCheckerFactoryImpl implements ConditionCheckerFactory {
    
    private FactoryInitializer factoryInitializer;
    
    public ConditionCheckerFactoryImpl(FactoryInitializer factories) {
        this.factoryInitializer = factories;
    }
    
    @Override
    public ConfigElementService getConfigElementService() {
        return factoryInitializer.getConfigElementFactory().getService();
    }
    
    @Override
    public ConditionChecker getConditionChecker() {
        return new ConditionCheckerImpl(this);
    }

    @Override
    public Toaster getToaster() {
        return factoryInitializer.getToaster();
    }
}
