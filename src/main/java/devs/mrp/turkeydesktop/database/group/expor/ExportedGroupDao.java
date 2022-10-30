/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface ExportedGroupDao extends GeneralDao<ExportedGroup, Long> {
    public Observable<ResultSet> findByGroup(Long id);
    public Observable<ResultSet> findByGroupAndFile(Long groupId, String file);
    public Observable<Long> deleteByGroup(Long id);
}
