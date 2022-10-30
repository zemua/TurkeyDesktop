/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class GroupAssignationService implements IGroupAssignationService {
    
    private static final GroupAssignationDao repo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupAssignationService.class.getName());
    
    @Override
    public Observable<Long> add(GroupAssignation element) {
        if (element == null) {
            return Observable.just(-1L);
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
            return Observable.just(0L);
        });
    }

    @Override
    public Observable<Long> update(GroupAssignation element) {
        if (element == null) {
            return Observable.just(-1L);
        }
        return repo.update(element);
    }

    @Override
    public Observable<List<GroupAssignation>> findAll() {
        return repo.findAll().map(this::elementsFromResultSet);
    }

    @Deprecated
    @Override
    public Observable<GroupAssignation> findById(long id) {
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
    public Observable<Long> deleteById(long id) {
        return repo.deleteById(id);
    }

    @Override
    public Observable<GroupAssignation> findByProcessId(String processId) {
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
    public Observable<Long> deleteByProcessId(String processId) {
        return repo.deleteByElementId(GroupAssignation.ElementType.PROCESS, processId);
    }

    @Override
    public Observable<GroupAssignation> findByTitleId(String titleId) {
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
    public Observable<GroupAssignation> findLongestTitleIdContainedIn(String titleId) {
        return repo.findAllOfType(GroupAssignation.ElementType.TITLE)
                .map(this::elementsFromResultSet)
                .map(titleAssignations -> titleAssignations.stream()
                        .filter(ga -> StringUtils.containsIgnoreCase(titleId, ga.getElementId()))
                        .max((ga1, ga2) -> Integer.compare(ga1.getElementId().length(), ga2.getElementId().length()))
                        .orElse(null));
    }
    
    @Override
    public Observable<Long> deleteByTitleId(String titleId) {
        return repo.deleteByElementId(GroupAssignation.ElementType.TITLE, titleId);
    }

    @Override
    public Observable<List<GroupAssignation>> findProcessesOfGroup(Long groupId) {
        return repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.PROCESS, groupId)
                .map(this::elementsFromResultSet);
    }

    @Override
    public Observable<List<GroupAssignation>> findTitlesOfGroup(Long groupId) {
        return repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.TITLE, groupId).map(this::elementsFromResultSet);
    }
    
    private List<GroupAssignation> elementsFromResultSet(ResultSet set) {
        List<GroupAssignation> elements = new ArrayList<>();
        try {
            while (set.next()) {
                GroupAssignation el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
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
    public Observable<Long> deleteByGroupId(long id) {
        return repo.deleteByGroupId(id);
    }

    @Override
    public Observable<GroupAssignation> findGroupOfAssignation(String assignation) {
        return repo.findByElementId(GroupAssignation.ElementType.TITLE, assignation.toLowerCase()).map(this::elementsFromResultSet).map(titleAssignations -> {
            if (titleAssignations.isEmpty()) {
                return null;
            }
            return titleAssignations.get(0);
        });
    }
    
}
