/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

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
public class ExternalGroupServiceImpl implements ExternalGroupService {
    
    private static final ExternalGroupDao repo = ExternalGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExternalGroupServiceImpl.class.getName());
    
    @Override
    public long add(ExternalGroup element) {
        if (element == null) {
            return -1;
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element.getId());
            try {
                if (rs.next()) {
                    ExternalGroup group = elementFromResultSetEntry(rs);
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
    public long update(ExternalGroup element) {
        if (element == null) {
            return -1;
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return -1;
        }
        return repo.update(element);
    }

    @Override
    public List<ExternalGroup> findAll() {
        return elementsFromResultSet(repo.findAll());
    }

    @Override
    public ExternalGroup findById(long id) {
        ResultSet set = repo.findById(id);
        ExternalGroup element = null;
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
    
    private List<ExternalGroup> elementsFromResultSet(ResultSet set) {
        List<ExternalGroup> elements = new ArrayList<>();
        try {
            while (set.next()) {
                ExternalGroup el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
    }
    
    private ExternalGroup elementFromResultSetEntry(ResultSet set) {
        ExternalGroup el = new ExternalGroup();
        try {
            el.setId(set.getLong(ExternalGroup.ID));
            el.setGroup(set.getLong(ExternalGroup.GROUP));
            el.setFile(set.getString(ExternalGroup.FILE));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public List<ExternalGroup> findByGroup(Long id) {
        return elementsFromResultSet(repo.findByGroup(id));
    }

    @Override
    public List<ExternalGroup> findByFile(String file) {
        return elementsFromResultSet(repo.findByFile(file));
    }

    @Override
    public long deleteByGroup(Long id) {
        return repo.deleteByGroup(id);
    }
    
}
