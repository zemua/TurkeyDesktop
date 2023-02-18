package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class TrayChainFactoryImpl implements TrayChainFactory {
    
    private FactoryInitializer factory;
    
    public TrayChainFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public TrayChainBaseHandler getChain() {
        return new TrayChainCommander(this).getHandlerChain();
    }

    @Override
    public WatchDog getWatchDog() {
        return factory.getWatchDogFactory().getInstance();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public TrayChainBaseHandler getLinuxHandler() {
        return new TrayChainHandlerLinux(this);
    }

    @Override
    public TrayChainBaseHandler getMacHandler() {
        return new TrayChainHandlerMacos(this);
    }
    
}
