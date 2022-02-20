/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

/**
 *
 * @author miguel
 */
public class FGroupConditionFacadeService {
    
    public static IGroupConditionFacadeService getService() {
        return new GroupConditionFacadeService();
    }
    
}
