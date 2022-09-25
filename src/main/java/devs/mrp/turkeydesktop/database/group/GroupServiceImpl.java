/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

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
public class GroupServiceImpl implements GroupService {
    
    private static final GroupDao repo = GroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupServiceImpl.class.getName());
    
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
                logger.log(Level.SEVERE, null, ex);
            }
            // else there is no element stored with this id
            return repo.add(element);
        }
    }

    @Override
    public long update(Group element) {
        if (element == null) {
            return -1;
        }
        return repo.update(element);
    }

    @Override
    public List<Group> findAll() {
        return elementsFromResultSet(repo.findAll());
    }

    @Override
    public Group findById(long id) {
        ResultSet set = repo.findById(id);
        Group element = null;
        try {
            if (set.next()) {
                element = elementFromResultSetEntry(set);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return element;
    }

    @Override
    public long deleteById(long id) {
        return repo.deleteById(id);
    }

    @Override
    public List<Group> findAllPositive() {
        return elementsFromResultSet(repo.findAllOfType(Group.GroupType.POSITIVE));
    }

    @Override
    public List<Group> findAllNegative() {
        return elementsFromResultSet(repo.findAllOfType(Group.GroupType.NEGATIVE));
    }
    
    @Override
    public int setPreventClose(long groupId, boolean preventClose) {
        return repo.setPreventClose(groupId, preventClose);
    }
    
    private List<Group> elementsFromResultSet(ResultSet set) {
        List<Group> elements = new ArrayList<>();
        try {
            while (set.next()) {
                Group el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
    }
    
    private Group elementFromResultSetEntry(ResultSet set) {
        Group el = new Group();
        try {
            el.setId(set.getLong(Group.ID));
            el.setName(set.getString(Group.NAME));
            el.setType(Group.GroupType.valueOf(set.getString(Group.TYPE)));
            el.setPreventClose(set.getBoolean(Group.PREVENT_CLOSE));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
