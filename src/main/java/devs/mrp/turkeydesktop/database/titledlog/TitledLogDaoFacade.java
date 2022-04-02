/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface TitledLogDaoFacade {
    
    public ResultSet getTimeFrameOfDependablesGroupedByProcess(long from, long to);
    
}
