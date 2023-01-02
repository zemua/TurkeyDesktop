/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation.GroupAssignationBuilder;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao.ElementId;
import io.reactivex.rxjava3.core.Maybe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class GroupAssignationService implements IGroupAssignationService {
    
    private static final Logger logger = Logger.getLogger(GroupAssignationService.class.getName());
    
    public static final DbCache<GroupAssignationDao.ElementId,GroupAssignation> dbCache = DbCacheFactory.getDbCache(
            GroupAssignationRepository.getInstance(),
            element -> new GroupAssignationDao.ElementId(element.getType(), element.getElementId()),
            key -> isValidKey(key),
            set -> elementsFromResultSet(set));
    
    private static boolean isValidKey(GroupAssignationDao.ElementId rowId) {
        return rowId.getElementId() != null && rowId.getType() != null && !rowId.getElementId().isEmpty();
    }
    
    @Override
    public Single<Long> add(GroupAssignation element) {
        if (element == null) {
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return dbCache.save(element).map(SaveAction::get);
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
                // sort longest first
                .sorted((ga1, ga2) -> Integer.compare(ga1.getElementId().length(), ga2.getElementId().length()))
                // get the first one only
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
    
    private static Observable<GroupAssignation> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }
    
    private static GroupAssignation elementFromResultSetEntry(ResultSet set) {
        GroupAssignationBuilder el = GroupAssignation.builder();
        try {
            el.type(set.getString(GroupAssignation.TYPE) != null ? GroupAssignation.ElementType.valueOf(set.getString(GroupAssignation.TYPE)) : null);
            el.elementId(set.getString(GroupAssignation.ELEMENT_ID));
            el.groupId(set.getLong(GroupAssignation.GROUP_ID));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el.build();
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
