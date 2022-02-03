/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

/**
 *
 * @author miguel
 */
public class FConfigElementService {
    
    public static IConfigElementService getService() {
        return new ConfigElementService();
    }
    
}
