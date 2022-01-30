/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface AssignableElementDao {
    
    public ResultSet findPositiveProcessesWithAssignation();
    public ResultSet findNegativeProcessesWithAssignation();
    public ResultSet findPositiveTitlesWithAssignation();
    public ResultSet findNegativeTitlesWithAssignation();
    
}
