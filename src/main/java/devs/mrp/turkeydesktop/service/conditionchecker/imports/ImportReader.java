/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.imports;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ImportReader {
    public List<ImportValue> getValuesFromFile(String path);
}
