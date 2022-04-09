/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

/**
 *
 * @author miguel
 */
public class AssignableElementServiceFactory {
    
    public static AssignableElementService getProcessesService() {
        return new AssignableProcessService();
    }
    
    public static AssignableElementService getTitlesService() {
        return new AssignableTitleServiceImpl();
    }
    
}
