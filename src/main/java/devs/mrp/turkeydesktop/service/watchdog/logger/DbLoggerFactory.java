/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog.logger;

/**
 *
 * @author miguel
 */
public class DbLoggerFactory {
    
    public static DbLogger getNew() {
        return new DbLoggerImpl();
    }
    
}
