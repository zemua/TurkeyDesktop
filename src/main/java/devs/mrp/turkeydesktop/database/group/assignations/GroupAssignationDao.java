/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface GroupAssignationDao extends GeneralDao<GroupAssignation, Long> {
    
    public ResultSet findByElementId(GroupAssignation.ElementType elementType, String elementId);
    public ResultSet findAllElementTypeOfGroup(GroupAssignation.ElementType elementType, Long groupId);
    
}
