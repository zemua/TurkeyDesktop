/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class ConditionRepository implements ConditionDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(ConditionRepository.class.getName());
    
    private static ConditionRepository instance;
    
    private ConditionRepository() {
        
    }
    
    public static ConditionRepository getInstance() {
        if (instance == null) {
            instance = new ConditionRepository();
        }
        return instance;
    }
    
    @Override
    public Observable<Long> add(Condition element) {
        return Db.observableLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s, %s) ",
                        Db.CONDITIONS_TABLE, Condition.GROUP_ID, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION)
                        + "VALUES (?, ?, ?, ?)");
                stm.setLong(1, element.getGroupId());
                stm.setLong(2, element.getTargetId());
                stm.setLong(3, element.getUsageTimeCondition());
                stm.setLong(4, element.getLastDaysCondition());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Observable<Long> update(Condition element) {
        return Db.observableLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.CONDITIONS_TABLE, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION, Condition.ID));
                stm.setLong(1, element.getTargetId());
                stm.setLong(2, element.getUsageTimeCondition());
                stm.setLong(3, element.getLastDaysCondition());
                stm.setLong(4, element.getId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Observable<ResultSet> findAll() {
        return Db.observableResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CONDITIONS_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }

    @Override
    public Observable<ResultSet> findById(Long id) {
        return Db.observableResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.ID));
                stm.setLong(1, id);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }
    
    @Override
    public Observable<ResultSet> findByGroupId(long groupId) {
        return Db.observableResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.GROUP_ID));
                stm.setLong(1, groupId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Observable<Long> deleteById(Long id) {
        return Db.observableLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
    @Override
    public Observable<Long> deleteByGroupId(long id) {
        return Db.observableLong(() -> {
            long delQty = -1;
                PreparedStatement stm;
                try {
                    stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                            Db.CONDITIONS_TABLE, Condition.GROUP_ID));
                    stm.setLong(1, id);
                    delQty = stm.executeUpdate();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            return delQty;
        });
    }
    
    @Override
    public Observable<Long> deleteByTargetId(long id) {
        return Db.observableLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.TARGET_ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
}
