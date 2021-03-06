/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

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
public class ConditionService implements IConditionService {
    
    private static final ConditionDao repo = ConditionRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ConditionService.class.getName());
    
    @Override
    public long add(Condition element) {
        if (element == null) {
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element.getId());
            try {
                if (rs.next()) {
                    Condition condition = elementFromResultSetEntry(rs);
                    // if the value stored differs from the one received
                    if (!condition.equals(element)) {
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
    public long update(Condition element) {
        if (element == null) {
            return -1;
        }
        return repo.update(element);
    }

    @Override
    public List<Condition> findAll() {
        return elementsFromResultSet(repo.findAll());
    }

    @Override
    public Condition findById(Long id) {
        ResultSet set = repo.findById(id);
        Condition element = null;
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
    public List<Condition> findByGroupId(Long groupId) {
        return elementsFromResultSet(repo.findByGroupId(groupId));
    }

    @Override
    public long deleteById(Long id) {
        return repo.deleteById(id);
    }
    
    @Override
    public long deleteByGroupId(long id) {
        return repo.deleteByGroupId(id);
    }
    
    @Override
    public long deleteByTargetId(long id) {
        return repo.deleteByTargetId(id);
    }
    
    private List<Condition> elementsFromResultSet(ResultSet set) {
        List<Condition> elements = new ArrayList<>();
        try {
            while (set.next()) {
                Condition el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
    }
    
    private Condition elementFromResultSetEntry(ResultSet set) {
        Condition el = new Condition();
        try {
            el.setId(set.getLong(Condition.ID));
            el.setGroupId(set.getLong(Condition.GROUP_ID));
            el.setTargetId(set.getLong(Condition.TARGET_ID));
            el.setUsageTimeCondition(set.getLong(Condition.USAGE_TIME_CONDITION));
            el.setLastDaysCondition(set.getLong(Condition.LAST_DAYS_CONDITION));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
