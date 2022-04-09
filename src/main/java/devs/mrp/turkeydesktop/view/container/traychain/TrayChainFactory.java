/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.ChainHandler;
import javax.swing.JFrame;

/**
 *
 * @author ncm55070
 */
public class TrayChainFactory {
    
    public static TrayChainBaseHandler getChain() {
        return new TrayChainCommander().getHandlerChain();
    }
    
}
