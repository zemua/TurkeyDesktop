/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface ConditionDao extends GeneralDao<Condition, Long> {
    
    public ResultSet findByGroupId(long groupId);
    public long deleteByGroupId(long groupId);
    public long deleteByTargetId(long targetId);
    
}
