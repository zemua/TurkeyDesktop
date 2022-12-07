/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface TitledLogDaoFacade {
    
    public Single<ResultSet> getTimeFrameOfDependablesGroupedByTitle(long from, long to);
    
}
