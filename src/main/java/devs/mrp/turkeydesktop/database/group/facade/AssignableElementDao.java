/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface AssignableElementDao {
    
    public Observable<ResultSet> findPositiveProcessesWithAssignation();
    public Observable<ResultSet> findNegativeProcessesWithAssignation();
    public Observable<ResultSet> findPositiveTitlesWithAssignation();
    public Observable<ResultSet> findNegativeTitlesWithAssignation();
    
}
