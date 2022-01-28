/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class GroupService implements IGroupService {
    
    private static final GroupDao repo = GroupRepository.getInstance();
    
    @Override
    public long add(Group element) {
        if (element == null) {
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element.getId());
            try {
                if (rs.next()) {
                    Group group = elementFromResultSetEntry(rs);
                    // if the value stored differs from the one received
                    if (!group.equals(element)) {
                        return update(element);
                    }
                    // else the value is the same as the one stored
                    return 0;
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimeLogService.class.getName()).log(Level.SEVERE, null, ex);
            }
            // else there is no element stored with this id
            return repo.add(element);
        }
    }

    @Override
    public long update(Group element) {
        if (element == null || element.getId() == 0) {
            return -1;
        }
        return repo.update(element);
    }

    @Override
    public List<Group> findAll() {
        // TODO
        ResultSet set = repo.findAll();
        try {
            while (set.next()) {
                Group el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigElementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        configMap.clear();
        elements.forEach(e -> configMap.put(e.getKey(), e.getValue()));
        return elements;
    }

    @Override
    public Group findById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long deleteById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Group> findAllPositive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Group> findAllNegative() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Group elementFromResultSetEntry(ResultSet set) {
        Group el = new Group();
        try {
            el.setId(set.getLong(Group.ID));
            el.setName(set.getString(Group.NAME));
            el.setType(Group.GroupType.valueOf(set.getString(Group.TYPE)));
        } catch (SQLException ex) {
            Logger.getLogger(GroupService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
