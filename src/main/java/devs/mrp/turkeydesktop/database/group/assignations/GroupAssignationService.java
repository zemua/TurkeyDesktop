package devs.mrp.turkeydesktop.database.group.assignations;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface GroupAssignationService {
    public Single<Long> add(GroupAssignation element);
    public Single<Long> update(GroupAssignation element);
    public Observable<GroupAssignation> findAll();
    @Deprecated
    public Single<GroupAssignation> findById(long id);
    @Deprecated
    public Single<Long> deleteById(long id);
    public Single<Long> deleteByGroupId(long id);
    
    public Maybe<GroupAssignation> findByProcessId(String processId);
    public Single<Long> deleteByProcessId(String processId);
    public Maybe<GroupAssignation> findByTitleId(String titleId);
    public Maybe<GroupAssignation> findLongestTitleIdContainedIn(String titleId);
    public Maybe<GroupAssignation> findGroupOfAssignation(String assignation);
    public Single<Long> deleteByTitleId(String titleId);
    public Observable<GroupAssignation> findProcessesOfGroup(Long groupId);
    public Observable<GroupAssignation> findTitlesOfGroup(Long groupId);
}
