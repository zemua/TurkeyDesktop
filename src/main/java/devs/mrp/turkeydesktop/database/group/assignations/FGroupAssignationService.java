/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

/**
 *
 * @author miguel
 */
public class FGroupAssignationService {
    
    public static IGroupAssignationService getService() {
        return new GroupAssignationService();
    }
    
}
