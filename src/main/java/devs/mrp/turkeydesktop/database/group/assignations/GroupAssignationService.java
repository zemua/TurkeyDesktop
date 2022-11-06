/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public class GroupAssignationService implements IGroupAssignationService {
    
    private static final GroupAssignationDao repo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupAssignationService.class.getName());
    
    @Override
    public Single<Long> add(GroupAssignation element) {
        if (element == null) {
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return repo.findByElementId(element.getType(), element.getElementId()).flatMap(rs -> {
            try {
                if (rs.next()) {
                    GroupAssignation group = elementFromResultSetEntry(rs);
                    // if the value stored differs from the one received
                    if (!group.equals(element)) {
                        return update(element);
                    }
                } else {
                    // else there is no element stored with this id
                    return repo.add(element);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return Single.just(0L);
        });
    }

    @Override
    public Single<Long> update(GroupAssignation element) {
        if (element == null) {
            return Single.just(-1L);
        }
        return repo.update(element);
    }

    @Override
    public Observable<GroupAssignation> findAll() {
        return repo.findAll().flatMapObservable(this::elementsFromResultSet);
    }

    @Deprecated
    @Override
    public Single<GroupAssignation> findById(long id) {
        return repo.findById(id).map(result -> {
            GroupAssignation element = null;
            try {
                if (result.next()) {
                    element = elementFromResultSetEntry(result);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return element;
        });
    }

    @Deprecated
    @Override
    public Single<Long> deleteById(long id) {
        return repo.deleteById(id);
    }

    @Override
    public Single<GroupAssignation> findByProcessId(String processId) {
        return repo.findByElementId(GroupAssignation.ElementType.PROCESS, processId).map(result -> {
            try {
                if (result.next()) {
                    return elementFromResultSetEntry(result);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GroupAssignationService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        });
    }
    
    @Override
    public Single<Long> deleteByProcessId(String processId) {
        return repo.deleteByElementId(GroupAssignation.ElementType.PROCESS, processId);
    }

    @Override
    public Single<GroupAssignation> findByTitleId(String titleId) {
        return repo.findByElementId(GroupAssignation.ElementType.TITLE, titleId).map(set -> {
            try {
                if (set.next()) {
                    return elementFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GroupAssignationService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        });
    }
    
    @Override
    public Single<GroupAssignation> findLongestTitleIdContainedIn(String titleId) {
        return repo.findAllOfType(GroupAssignation.ElementType.TITLE)
                .flatMapObservable(this::elementsFromResultSet)
                .filter(ga -> StringUtils.containsIgnoreCase(titleId, ga.getElementId()))
                // sort longest first
                .toSortedList((ga1, ga2) -> Integer.compare(ga1.getElementId().length(), ga2.getElementId().length()))
                // get the first one only
                .map(list -> list.get(0))
                .toSingle();
    }
    
    @Override
    public Single<Long> deleteByTitleId(String titleId) {
        return repo.deleteByElementId(GroupAssignation.ElementType.TITLE, titleId);
    }

    @Override
    public Observable<GroupAssignation> findProcessesOfGroup(Long groupId) {
        return repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.PROCESS, groupId)
                .flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Observable<GroupAssignation> findTitlesOfGroup(Long groupId) {
        return repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.TITLE, groupId).flatMapObservable(this::elementsFromResultSet);
    }
    
    private Observable<GroupAssignation> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscriber.onError(ex);
            }
            subscriber.onCompleted();
        });
    }
    
    private GroupAssignation elementFromResultSetEntry(ResultSet set) {
        GroupAssignation el = new GroupAssignation();
        try {
            el.setType(set.getString(GroupAssignation.TYPE) != null ? GroupAssignation.ElementType.valueOf(set.getString(GroupAssignation.TYPE)) : null);
            el.setElementId(set.getString(GroupAssignation.ELEMENT_ID));
            el.setGroupId(set.getLong(GroupAssignation.GROUP_ID));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public Single<Long> deleteByGroupId(long id) {
        return repo.deleteByGroupId(id);
    }

    @Override
    public Single<GroupAssignation> findGroupOfAssignation(String assignation) {
        return repo.findByElementId(GroupAssignation.ElementType.TITLE, assignation.toLowerCase())
                .map(resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return elementFromResultSetEntry(resultSet);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(GroupAssignationService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                });
    }
    
}
