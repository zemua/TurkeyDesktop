/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.GroupService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class GroupAssignationService implements IGroupAssignationService {
    
    private static final GroupAssignationDao repo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupAssignationService.class.getName());
    
    @Override
    public long add(GroupAssignation element) {
        if (element == null) {
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findByElementId(element.getType(), element.getElementId());
            try {
                if (rs.next()) {
                    GroupAssignation group = elementFromResultSetEntry(rs);
                    // if the value stored differs from the one received
                    if (!group.equals(element)) {
                        return update(element);
                    }
                    // else the value is the same as the one stored
                    return 0;
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            // else there is no element stored with this id
            return repo.add(element);
        }
    }

    @Override
    public long update(GroupAssignation element) {
        if (element == null) {
            return -1;
        }
        return repo.update(element);
    }

    @Override
    public List<GroupAssignation> findAll() {
        return elementsFromResultSet(repo.findAll());
    }

    @Deprecated
    @Override
    public GroupAssignation findById(long id) {
        ResultSet set = repo.findById(id);
        GroupAssignation element = null;
        try {
            if (set.next()) {
                element = elementFromResultSetEntry(set);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return element;
    }

    @Deprecated
    @Override
    public long deleteById(long id) {
        return repo.deleteById(id);
    }

    @Override
    public GroupAssignation findByProcessId(String processId) {
        return elementFromResultSetEntry(repo.findByElementId(GroupAssignation.ElementType.PROCESS, processId));
    }
    
    @Override
    public long deleteByProcessId(String processId) {
        return repo.deleteByElementId(GroupAssignation.ElementType.PROCESS, processId);
    }

    @Override
    public GroupAssignation findByTitleId(String titleId) {
        return elementFromResultSetEntry(repo.findByElementId(GroupAssignation.ElementType.TITLE, titleId));
    }
    
    @Override
    public GroupAssignation findLongestTitleIdContainedIn(String titleId) {
        List<GroupAssignation> titleAssignations = elementsFromResultSet(repo.findAllOfType(GroupAssignation.ElementType.TITLE));
        return titleAssignations.stream()
                .filter(ga -> titleId.contains(ga.getElementId()))
                .max((ga1, ga2) -> Integer.compare(ga1.getElementId().length(), ga2.getElementId().length()))
                .orElse(null);
    }
    
    @Override
    public long deleteByTitleId(String titleId) {
        return repo.deleteByElementId(GroupAssignation.ElementType.TITLE, titleId);
    }

    @Override
    public List<GroupAssignation> findProcessesOfGroup(Long groupId) {
        return elementsFromResultSet(repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.PROCESS, groupId));
    }

    @Override
    public List<GroupAssignation> findTitlesOfGroup(Long groupId) {
        return elementsFromResultSet(repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.TITLE, groupId));
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
            if (!set.first()) {
                return null;
            }
            el.setType(set.getString(GroupAssignation.TYPE) != null ? GroupAssignation.ElementType.valueOf(set.getString(GroupAssignation.TYPE)) : null);
            el.setElementId(set.getString(GroupAssignation.ELEMENT_ID));
            el.setGroupId(set.getLong(GroupAssignation.GROUP_ID));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
