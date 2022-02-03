/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

/**
 *
 * @author miguel
 */
public class FTitledLogServiceFacade {
    
    public static ITitledLogServiceFacade getService() {
        return new TitledLogServiceFacade();
    }
    
}
