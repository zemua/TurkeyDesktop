/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface IGroupAssignationService {
    public Observable<Long> add(GroupAssignation element);
    public Observable<Long> update(GroupAssignation element);
    public Observable<List<GroupAssignation>> findAll();
    @Deprecated
    public Observable<GroupAssignation> findById(long id);
    @Deprecated
    public Observable<Long> deleteById(long id);
    public Observable<Long> deleteByGroupId(long id);
    
    public Observable<GroupAssignation> findByProcessId(String processId);
    public Observable<Long> deleteByProcessId(String processId);
    public Observable<GroupAssignation> findByTitleId(String titleId);
    public Observable<GroupAssignation> findLongestTitleIdContainedIn(String titleId);
    public Observable<GroupAssignation> findGroupOfAssignation(String assignation);
    public Observable<Long> deleteByTitleId(String titleId);
    public Observable<List<GroupAssignation>> findProcessesOfGroup(Long groupId);
    public Observable<List<GroupAssignation>> findTitlesOfGroup(Long groupId);
}
