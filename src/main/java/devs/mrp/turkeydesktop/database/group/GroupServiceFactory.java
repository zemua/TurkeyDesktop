/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

/**
 *
 * @author miguel
 */
public class GroupServiceFactory {
    
    public static GroupService getService() {
        return new GroupServiceImpl();
    }
    
}
