/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions.impor;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ImportConditionService {
    public long add(ImportCondition element);
    public long update(ImportCondition element);
    public List<ImportCondition> findAll();
    public List<ImportCondition> findByTxtFile();
}
