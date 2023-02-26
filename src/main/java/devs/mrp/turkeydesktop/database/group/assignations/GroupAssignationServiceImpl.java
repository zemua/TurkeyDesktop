package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao.ElementId;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.apache.commons.lang3.StringUtils;

public class GroupAssignationServiceImpl implements GroupAssignationService {
    
    private final DbCache<GroupAssignationDao.ElementId,GroupAssignation> dbCache;
    
    public GroupAssignationServiceImpl(GroupAssignationFactory factory) {
        this.dbCache = factory.getDbCache();
    }
    
    @Override
    public Single<Long> add(GroupAssignation assignation) {
        if (GroupAssignationValidator.isInvalidAssignation(assignation)) {
            return Single.just(-1L);
        }
        return dbCache.save(assignation).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(GroupAssignation element) {
        if (element == null) {
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<GroupAssignation> findAll() {
        return dbCache.getAll();
    }

    @Deprecated
    @Override
    public Single<GroupAssignation> findById(long id) {
        return null;
    }

    @Deprecated
    @Override
    public Single<Long> deleteById(long id) {
        return null;
    }

    @Override
    public Maybe<GroupAssignation> findByProcessId(String processId) {
        return dbCache.read(ElementId.builder().type(GroupAssignation.ElementType.PROCESS).elementId(processId).build());
    }
    
    @Override
    public Single<Long> deleteByProcessId(String processId) {
        return dbCache.remove(ElementId.builder().type(GroupAssignation.ElementType.PROCESS).elementId(processId).build())
                .map(b -> b ? 1L : 0L);
    }

    @Override
    public Maybe<GroupAssignation> findByTitleId(String titleId) {
        return dbCache.read(ElementId.builder().type(GroupAssignation.ElementType.TITLE).elementId(titleId).build());
    }
    
    @Override
    public Maybe<GroupAssignation> findLongestTitleIdContainedIn(String titleId) {
        return dbCache.getAll()
                .filter(ga -> GroupAssignation.ElementType.TITLE.equals(ga.getType()))
                .filter(ga -> StringUtils.containsIgnoreCase(titleId, ga.getElementId()))
                .sorted((ga1, ga2) -> Integer.compare(ga1.getElementId().length(), ga2.getElementId().length()))
                .firstElement();
    }
    
    @Override
    public Single<Long> deleteByTitleId(String titleId) {
        return dbCache.remove(ElementId.builder().type(GroupAssignation.ElementType.TITLE).elementId(titleId).build())
                .map(b -> b ? 1L : 0L);
    }

    @Override
    public Observable<GroupAssignation> findProcessesOfGroup(Long groupId) {
        return dbCache.getAll()
                .filter(ga -> GroupAssignation.ElementType.PROCESS.equals(ga.getType()))
                .filter(ga -> groupId.equals(ga.getGroupId()));
    }

    @Override
    public Observable<GroupAssignation> findTitlesOfGroup(Long groupId) {
        return dbCache.getAll()
                .filter(ga -> GroupAssignation.ElementType.TITLE.equals(ga.getType()))
                .filter(ga -> groupId.equals(ga.getGroupId()));
    }

    @Override
    public Single<Long> deleteByGroupId(long id) {
        return dbCache.getAll()
                .filter(ga -> id == ga.getGroupId())
                .flatMapSingle(ga -> dbCache.remove(ElementId.builder().type(ga.getType()).elementId(ga.getElementId()).build()))
                .count();
    }

    @Override
    public Maybe<GroupAssignation> findGroupOfAssignation(String assignation) {
        return dbCache.read(ElementId.builder().type(GroupAssignation.ElementType.TITLE).elementId(assignation.toLowerCase()).build());
    }
    
}
