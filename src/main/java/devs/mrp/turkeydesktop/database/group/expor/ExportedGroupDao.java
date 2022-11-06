/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface ExportedGroupDao extends GeneralDao<ExportedGroup, Long> {
    public Single<ResultSet> findByGroup(Long id);
    public Single<ResultSet> findByGroupAndFile(Long groupId, String file);
    public Single<Long> deleteByGroup(Long id);
}
