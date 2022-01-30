/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.Group;
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
    
    @Override
    public long add(GroupAssignation element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GroupAssignation findById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long deleteById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GroupAssignation findByProcessId(String processId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GroupAssignation findByTitleId(String titleId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GroupAssignation> findProcessesOfGroup(Long groupId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GroupAssignation> findTitlesOfGroup(Long groupId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private List<GroupAssignation> elementsFromResultSet(ResultSet set) {
        List<GroupAssignation> elements = new ArrayList<>();
        try {
            while (set.next()) {
                GroupAssignation el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigElementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return elements;
    }
    
    private GroupAssignation elementFromResultSetEntry(ResultSet set) {
        GroupAssignation el = new GroupAssignation();
        try {
            el.setId(set.getLong(GroupAssignation.ID));
            el.setType(GroupAssignation.ElementType.valueOf(set.getString(GroupAssignation.TYPE)));
            el.setElementId(set.getString(GroupAssignation.ELEMENT_ID));
            el.setGroupId(set.getLong(GroupAssignation.GROUP_ID));
        } catch (SQLException ex) {
            Logger.getLogger(GroupService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
