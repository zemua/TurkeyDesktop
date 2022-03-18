/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions.impor;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface ImportConditionDao extends GeneralDao<ImportCondition, Long> {
    
    public ResultSet findByGroupId(long groupId);
    public long deleteByGroupId(long groupId);
    public long deleteByTargetTxt(String targetTxt);
    
}
