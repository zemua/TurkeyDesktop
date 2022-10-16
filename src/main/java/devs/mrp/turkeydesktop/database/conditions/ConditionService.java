/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
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
    public void add(Condition element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            TurkeyAppFactory.runResultSetWorker(() -> repo.findById(element.getId()), rs -> {
                try {
                    if (rs.next()) {
                        Condition condition = elementFromResultSetEntry(rs);
                        // if the value stored differs from the one received
                        if (!condition.equals(element)) {
                            update(element, consumer);
                        } else {
                            // else the value is the same as the one stored
                            consumer.accept(0);
                        }
                    } else {
                        // else there is no element stored with this id
                        TurkeyAppFactory.runLongWorker(() -> repo.add(element), consumer);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public void update(Condition element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            TurkeyAppFactory.runLongWorker(() -> repo.update(element), consumer);
        }
    }

    @Override
    public void findAll(Consumer<List<Condition>> consumer) {
        FConditionService.runConditionListWorker(() -> elementsFromResultSet(repo.findAll()), consumer);
    }

    @Override
    public void findById(Long id, Consumer<Condition> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            Condition element = null;
            try {
                if (set.next()) {
                    element = elementFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(element);
        });
    }
    
    @Override
    public void findByGroupId(Long groupId, Consumer<List<Condition>> consumer) {
        FConditionService.runConditionListWorker(() -> elementsFromResultSet(repo.findByGroupId(groupId)), consumer);
    }

    @Override
    public void deleteById(Long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }
    
    @Override
    public void deleteByGroupId(long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteByGroupId(id), consumer);
    }
    
    @Override
    public void deleteByTargetId(long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteByTargetId(id), consumer);
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
