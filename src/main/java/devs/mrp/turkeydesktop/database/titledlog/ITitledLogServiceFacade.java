/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import java.util.Date;
import java.util.List;

/**
 *
 * @author miguel
 */
public interface ITitledLogServiceFacade {
    
    public List<TitledLog> getLogsWithTitleConditions(Date from, Date to);
    public List<TitledLog> getLogsDependablesWithTitleConditions(Date from, Date to);
}
