/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.ChainCommander;

/**
 *
 * @author ncm55070
 */
public class TrayChainCommander implements ChainCommander {

    private TrayChainBaseHandler linuxHandler;
    private TrayChainBaseHandler macosHandler;
    
    @Override
    public TrayChainBaseHandler getHandlerChain() {
        linuxHandler = TrayChainHandlerLinux.getInstance();
        macosHandler = TrayChainHandlerMacos.getInstance();
        
        linuxHandler.setNextHandler(macosHandler);
        
        return linuxHandler;
    }
    
}
