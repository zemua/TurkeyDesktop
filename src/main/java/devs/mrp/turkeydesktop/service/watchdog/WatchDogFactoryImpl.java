package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class WatchDogFactoryImpl implements WatchDogFactory {
    
    private FactoryInitializer factory;
    
    public WatchDogFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public WatchDog getInstance() {
        return WatchDogImpl.getInstance(this);
    }

    @Override
    public ConditionChecker getConditionChecker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
    }

    @Override
    public Toaster getToaster() {
        return factory.getToaster();
    }

    @Override
    public FileHandler getFileHandler() {
        return factory.getFileHandler();
    }
    
}
