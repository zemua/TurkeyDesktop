/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.idle;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class IdleChainHandlerLinux extends ChainHandler<LongWrapper> {
    
    private Runtime r = Runtime.getRuntime();
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(LongWrapper data) {
        try {
            Process p = r.exec("xprintidle");
            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = b.readLine();
            data.setValue(line != null ? Long.valueOf(line) : 0);
            b.close();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(IdleChainHandlerLinux.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
