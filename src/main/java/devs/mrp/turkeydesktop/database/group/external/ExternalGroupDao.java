/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface ExternalGroupDao extends GeneralDao<ExternalGroup, Long> {
    public Single<ResultSet> findByGroup(Long id);
    public Single<ResultSet> findByFile(String file);
    public Single<ResultSet> findByGroupAndFile(Long id, String file);
    public Single<Long> deleteByGroup(Long id);
}
