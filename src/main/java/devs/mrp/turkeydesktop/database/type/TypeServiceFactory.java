/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

/**
 *
 * @author miguel
 */
public class TypeServiceFactory {
    
    public static TypeService getService() {
        return new TypeServiceImpl();
    }
    
}
