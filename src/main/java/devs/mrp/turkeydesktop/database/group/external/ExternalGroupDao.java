/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface ExternalGroupDao extends GeneralDao<ExternalGroup, Long> {
    public Observable<ResultSet> findByGroup(Long id);
    public Observable<ResultSet> findByFile(String file);
    public Observable<ResultSet> findByGroupAndFile(Long id, String file);
    public Observable<Long> deleteByGroup(Long id);
}
