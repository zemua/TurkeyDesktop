/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import com.sun.jna.Platform;
import com.sun.jna.platform.unix.X11;
import devs.mrp.turkeydesktop.common.ChainHandler;
import io.github.kingpulse.structs.xdo_t;
import io.github.kingpulse.xdotool;

/**
 * xdotool helps to get the name of the windows you are working on
 * if you are in Ubuntu, you can install it with the following:
 * sudo apt-get install xdotool
 * sudo apt-get install libxdo-dev
 * https://github.com/jordansissel/xdotool
 */
public class CheckerChainHandlerLinuxXdotool extends ChainHandler<IProcessInfo> {
    
    private static final String DISPLAY_STRING = ":1";
    
    private static xdotool lib = xdotool.loadLib();
    private static Process proc = null;
    private static xdo_t xdo = null;
    private X11.Window win = null;
    
    public CheckerChainHandlerLinuxXdotool() {
        if (xdo == null) {xdo = lib.xdo_new(DISPLAY_STRING);}
        
    }
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(IProcessInfo processInfo) {
        // TODO implement
    }
    
}
