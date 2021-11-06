/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public class WatchDog implements IWatchDog {
    
    private boolean on = true;
    private JTextArea mLogger;

    @Override
    public void begin(JTextArea logger) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLogger(JTextArea logger) {
        mLogger = logger;
    }

    @Override
    public void stop() {
        on = false;
    }
    
    private void log(String text) {
        mLogger.append(String.format("%s \n", text));
    }

}
