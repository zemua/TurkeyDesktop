/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface GroupDao extends GeneralDao<Group, Long> {
    
    public Single<ResultSet> findAllOfType(Group.GroupType type);
    
    public Single<Integer> setPreventClose(long groupId, boolean preventClose);
    
}
