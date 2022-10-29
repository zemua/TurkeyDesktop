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
import rx.Observable;

/**
 *
 * @author miguel
 */
public class ConditionService implements IConditionService {
    
    private static final ConditionDao repo = ConditionRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ConditionService.class.getName());
    
    @Override
    public Observable<Long> add(Condition element) {
        if (element == null) {
            return Observable.just(-1L);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            return repo.findById(element.getId()).flatMap(rs -> {
                try {
                    if (rs.next()) {
                        Condition condition = elementFromResultSetEntry(rs);
                        // if the value stored differs from the one received
                        if (!condition.equals(element)) {
                            return update(element);
                        }
                    } else {
                        // else there is no element stored with this id
                        return repo.add(element);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ConditionService.class.getName()).log(Level.SEVERE, null, ex);
                }
                return Observable.just(0L);
            });
        }
    }

    @Override
    public Observable<Long> update(Condition element) {
        if (element == null) {
            return Observable.just(-1L);
        } else {
            return repo.update(element);
        }
    }

    @Override
    public Observable<List<Condition>> findAll() {
        return repo.findAll().map(this::elementsFromResultSet);
    }

    @Override
    public Observable<Condition> findById(Long id) {
        return repo.findById(id).map(set -> {
            Condition element = null;
            try {
                if (set.next()) {
                    element = elementFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return element;
        });
    }
    
    @Override
    public Observable<List<Condition>> findByGroupId(Long groupId) {
        return repo.findByGroupId(groupId).map(this::elementsFromResultSet);
    }

    @Override
    public Observable<Long> deleteById(Long id) {
        return repo.deleteById(id);
    }
    
    @Override
    public Observable<Long> deleteByGroupId(long id) {
        return repo.deleteByGroupId(id);
    }
    
    @Override
    public Observable<Long> deleteByTargetId(long id) {
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
