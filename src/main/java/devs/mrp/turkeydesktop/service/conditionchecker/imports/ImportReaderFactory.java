/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.imports;

/**
 *
 * @author miguel
 */
public class ImportReaderFactory {
    public static ImportReader getReader() {
        return new ImportReaderImpl();
    }
}
