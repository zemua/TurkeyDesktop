/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.service.conditionchecker.idle;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ncm55070
 */
public class IdleChainHandlerMacos extends ChainHandler<LongWrapper> {

    private Runtime r = Runtime.getRuntime();

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isMac();
    }

    @Override
    protected void handle(LongWrapper data) {
        try {
            List<String> lines = new ArrayList<>();
            String[] comms = {"ioreg", "-c", "IOHIDSystem"};
            Process p = r.exec(comms);
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            data.setValue(0);
            while ((line = b.readLine()) != null) {
                if (line.contains("HIDIdleTime")) {
                    String[] fragments = line.split(" ");
                    Long time = Optional.ofNullable(Long.valueOf(fragments[fragments.length - 1])).orElse(0L) / 1000000;
                    data.setValue(time);
                    break;
                }
            }
            b.close();
        } catch (IOException ex) {
            Logger.getLogger(IdleChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            // if there is an exception parsing the values of runtime catch it here to not break the watchdog
            Logger.getLogger(IdleChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
