package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.ChainCommander;

public class TrayChainCommander implements ChainCommander {

    private final TrayChainFactory factory;
    private TrayChainBaseHandler linuxHandler;
    private TrayChainBaseHandler macosHandler;
    
    TrayChainCommander(TrayChainFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public TrayChainBaseHandler getHandlerChain() {
        linuxHandler = factory.getLinuxHandler();
        macosHandler = factory.getMacHandler();
        
        linuxHandler.setNextHandler(macosHandler);
        
        return linuxHandler;
    }
    
}
