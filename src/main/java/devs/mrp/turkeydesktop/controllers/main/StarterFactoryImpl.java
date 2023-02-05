package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class StarterFactoryImpl implements StarterFactory {
    
    private FactoryInitializer factory;
    
    public StarterFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public Starter getStarterController() {
        return new StarterImpl(this);
    }

    @Override
    public WatchDog getWatchDog() {
        return factory.getWatchDogFactory().getInstance();
    }
}
