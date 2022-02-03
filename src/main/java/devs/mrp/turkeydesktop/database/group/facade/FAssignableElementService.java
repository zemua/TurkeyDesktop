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
public class FAssignableElementService {
    
    public static IAssignableElementService getProcessesService() {
        return new AssignableProcessService();
    }
    
    public static IAssignableElementService getTitlesService() {
        return new AssignableTitleService();
    }
    
}
