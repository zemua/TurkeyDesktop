/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

/**
 *
 * @author miguel
 */
public class FLogAndTypeService {
    
    public static ILogAndTypeService getService() {
        return new LogAndTypeFacadeService();
    }
    
}