/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

/**
 *
 * @author miguel
 */
public class ImportServiceFactory {
    public static ImportService getService() {
        return new ImportServiceImpl();
    }
}
