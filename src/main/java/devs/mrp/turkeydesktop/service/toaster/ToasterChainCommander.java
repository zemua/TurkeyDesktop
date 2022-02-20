/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.toaster;

import devs.mrp.turkeydesktop.common.ChainCommander;
import devs.mrp.turkeydesktop.common.ChainHandler;

/**
 *
 * @author miguel
 */
public class ToasterChainCommander implements ChainCommander {
    
    private ChainHandler<String> linuxHandler;
    
    @Override
    public ChainHandler<String> getHandlerChain() {
        linuxHandler = new ToasterChainHandlerLinux();
        
        return linuxHandler;
    }
    
}
