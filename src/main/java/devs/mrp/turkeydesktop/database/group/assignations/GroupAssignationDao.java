/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface GroupAssignationDao extends GeneralDao<GroupAssignation, Long> {
    
    public Observable<ResultSet> findByElementId(GroupAssignation.ElementType elementType, String elementId);
    public Observable<Long> deleteByElementId(GroupAssignation.ElementType elementType, String elementId);
    public Observable<ResultSet> findAllElementTypeOfGroup(GroupAssignation.ElementType elementType, Long groupId);
    public Observable<ResultSet> findAllOfType(GroupAssignation.ElementType elementType);
    public Observable<Long> deleteByGroupId(long groupId);
    
}
