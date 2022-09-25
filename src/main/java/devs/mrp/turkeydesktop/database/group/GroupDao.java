/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface GroupDao extends GeneralDao<Group, Long> {
    
    public ResultSet findAllOfType(Group.GroupType type);
    
    public int setPreventClose(long groupId, boolean preventClose);
    
}
