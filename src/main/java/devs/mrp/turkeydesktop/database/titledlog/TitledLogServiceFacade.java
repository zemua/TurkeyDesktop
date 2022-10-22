/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public interface TitledLogServiceFacade {
    
    public void getLogsWithTitleConditions(Date from, Date to, Consumer<List<TitledLog>> consumer);
    public void getLogsDependablesWithTitleConditions(Date from, Date to, Consumer<List<TitledLog>> consumer);
}
