/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface GroupDao extends GeneralDao<Group, Long> {
    
    public Observable<ResultSet> findAllOfType(Group.GroupType type);
    
    public Observable<Integer> setPreventClose(long groupId, boolean preventClose);
    
}
