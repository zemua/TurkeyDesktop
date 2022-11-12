/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import java.util.Date;
import io.reactivex.rxjava3.core.Observable;

/**
 *
 * @author miguel
 */
public interface TitledLogServiceFacade {
    
    public Observable<TitledLog> getLogsWithTitleConditions(Date from, Date to);
    public Observable<TitledLog> getLogsDependablesWithTitleConditions(Date from, Date to);
}
