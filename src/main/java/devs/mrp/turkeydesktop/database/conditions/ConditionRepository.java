/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ConditionRepository implements ConditionDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(ConditionRepository.class.getName());
    private Semaphore semaphore = Db.getSemaphore();
    
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
    public long add(Condition element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s, %s) ",
                        Db.CONDITIONS_TABLE, Condition.CONDITION_TYPE, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION)
                        + "VALUES (?, ?, ?, ?)");
                stm.setString(1, element.getConditionType().toString());
                stm.setLong(2, element.getTargetId());
                stm.setLong(3, element.getUsageTimeCondition());
                stm.setLong(4, element.getLastDaysCondition());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return result;
    }

    @Override
    public long update(Condition element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.CONDITIONS_TABLE, Condition.CONDITION_TYPE, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION, Condition.ID));
                stm.setString(1, element.getConditionType().toString());
                stm.setLong(2, element.getTargetId());
                stm.setLong(3, element.getUsageTimeCondition());
                stm.setLong(4, element.getLastDaysCondition());
                stm.setLong(5, element.getId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return result;
    }

    @Override
    public ResultSet findAll() {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CONDITIONS_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null ,ex);
        } finally {
            semaphore.release();
        }
        return rs;
    }

    @Override
    public ResultSet findById(Long id) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.ID));
                stm.setLong(1, id);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return rs;
    }

    @Override
    public long deleteById(Long id) {
        long delQty = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return delQty;
    }
    
}
