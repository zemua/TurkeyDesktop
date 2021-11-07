/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;

/**
 *
 * @author miguel
 */
public class FCheckerChain {
    
    public static ChainHandler getChain() {
        return new CheckerChainCommander().getHandlerChain();
    }
    
}
