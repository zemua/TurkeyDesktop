/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

/**
 *
 * @author miguel
 */
public class ExportedGroupServiceFactory {
    
    public static ExportedGroupService getService() {
        return new ExportedGroupServiceImpl();
    }
    
}
