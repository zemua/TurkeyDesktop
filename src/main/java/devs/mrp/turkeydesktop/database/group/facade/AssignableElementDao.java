/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import java.sql.ResultSet;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface AssignableElementDao {
    
    public Single<ResultSet> findPositiveProcessesWithAssignation();
    public Single<ResultSet> findNegativeProcessesWithAssignation();
    public Single<ResultSet> findPositiveTitlesWithAssignation();
    public Single<ResultSet> findNegativeTitlesWithAssignation();
    
}
