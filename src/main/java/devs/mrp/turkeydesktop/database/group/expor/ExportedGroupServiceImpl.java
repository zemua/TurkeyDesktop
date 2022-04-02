/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

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
public class ExportedGroupServiceImpl implements ExportedGroupService {

    private static final ExportedGroupDao repo = ExportedGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExportedGroupServiceImpl.class.getName());

    @Override
    public long add(ExportedGroup element) {
        if (element == null) {
            return -1;
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element.getGroup());
            try {
                if (rs.next()) {
                    ExportedGroup group = elementFromResultSetEntry(rs);
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
    public long update(ExportedGroup element) {
        if (element == null) {
            return -1;
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return -1;
        }
        return repo.update(element);
    }

    @Override
    public List<ExportedGroup> findAll() {
        return elementsFromResultSet(repo.findAll());
    }

    @Override
    public ExportedGroup findById(long id) {
        ResultSet set = repo.findById(id);
        ExportedGroup element = null;
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
    public List<ExportedGroup> findByGroup(long id) {
        return elementsFromResultSet(repo.findByGroup(id));
    }

    @Override
    public List<ExportedGroup> findByFileAndGroup(long groupId, String file) {
        return elementsFromResultSet(repo.findByGroupAndFile(groupId, file));
    }

    @Override
    public long deleteByGroup(long id) {
        return repo.deleteByGroup(id);
    }

    private List<ExportedGroup> elementsFromResultSet(ResultSet set) {
        List<ExportedGroup> elements = new ArrayList<>();
        try {
            while (set.next()) {
                ExportedGroup el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
    }

    private ExportedGroup elementFromResultSetEntry(ResultSet set) {
        ExportedGroup el = new ExportedGroup();
        try {
            el.setGroup(set.getLong(ExportedGroup.GROUP));
            el.setFile(set.getString(ExportedGroup.FILE));
            el.setDays(set.getLong(ExportedGroup.DAYS));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

}
