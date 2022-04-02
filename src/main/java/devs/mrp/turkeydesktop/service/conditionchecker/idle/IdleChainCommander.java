/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.idle;

import devs.mrp.turkeydesktop.common.ChainCommander;
import devs.mrp.turkeydesktop.common.ChainHandler;

/**
 *
 * @author miguel
 */
public class IdleChainCommander implements ChainCommander {
    
    private ChainHandler<LongWrapper> linuxHandler;
    
    @Override
    public ChainHandler<LongWrapper> getHandlerChain() {
        linuxHandler = new IdleChainHandlerLinux();
        
        return linuxHandler;
    }
    
}
