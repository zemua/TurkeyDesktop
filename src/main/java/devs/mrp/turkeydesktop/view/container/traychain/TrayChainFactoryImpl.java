package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactoryImpl;

public class TrayChainFactoryImpl implements TrayChainFactory {
    
    private static TrayChainFactoryImpl instance;
    
    private TrayChainFactoryImpl() {}
    
    public static TrayChainFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TrayChainFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public TrayChainBaseHandler getChain() {
        return new TrayChainCommander(this).getHandlerChain();
    }

    @Override
    public WatchDog getWatchDog() {
        return WatchDogFactoryImpl.getInstance().getWatchDog();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
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
