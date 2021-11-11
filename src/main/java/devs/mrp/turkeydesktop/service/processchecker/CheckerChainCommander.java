/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.common.ChainCommander;
import devs.mrp.turkeydesktop.common.ChainHandler;

/**
 *
 * @author miguel
 */
public class CheckerChainCommander implements ChainCommander {

    ChainHandler linuxHandler;
    
    @Override
    public ChainHandler getHandlerChain() {
        //linuxHandler = new CheckerChainHandlerLinux(); // implementation has limitations to get process info
        linuxHandler = new CheckerChainHandlerLinuxXdotool();
        
        return linuxHandler;
    }
    
}
