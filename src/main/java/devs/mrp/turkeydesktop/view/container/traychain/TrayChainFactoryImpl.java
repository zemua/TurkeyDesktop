package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactoryImpl;

public class TrayChainFactoryImpl implements TrayChainFactory {
    
    private static TrayChainFactoryImpl instance;
    private static TrayChainBaseHandler trayChainHandler;
    private static TrayChainBaseHandler linuxHandler;
    private static TrayChainBaseHandler macosHandler;
    
    private TrayChainFactoryImpl() {}
    
    public static TrayChainFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TrayChainFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public TrayChainBaseHandler getChain() {
        if (trayChainHandler == null) {
            trayChainHandler = new TrayChainCommander(this).getHandlerChain();
        }
        return trayChainHandler;
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
        if (linuxHandler == null) {
            linuxHandler = new TrayChainHandlerLinux(this);
        }
        return linuxHandler;
    }

    @Override
    public TrayChainBaseHandler getMacHandler() {
        if (macosHandler == null) {
            macosHandler = new TrayChainHandlerMacos(this);
        }
        return macosHandler;
    }
    
}
