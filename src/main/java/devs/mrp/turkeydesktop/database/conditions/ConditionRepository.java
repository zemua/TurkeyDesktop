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
import io.reactivex.rxjava3.core.Single;
import java.sql.Statement;

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
    
    static ConditionRepository getInstance() {
        if (instance == null) {
            instance = new ConditionRepository();
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(Condition element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s, %s) ",
                        Db.CONDITIONS_TABLE, Condition.GROUP_ID, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION)
                        + "VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                stm.setLong(1, element.getGroupId());
                stm.setLong(2, element.getTargetId());
                stm.setLong(3, element.getUsageTimeCondition());
                stm.setLong(4, element.getLastDaysCondition());
                stm.executeUpdate();
                ResultSet generatedId = stm.getGeneratedKeys();
                if (generatedId.next()) {
                    result = generatedId.getLong(1);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Single<Long> update(Condition element) {
        return Db.singleLong(() -> {
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
    public Single<ResultSet> findAll() {
        return Db.singleResultSet(() -> {
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
    public Single<ResultSet> findById(Long id) {
        return Db.singleResultSet(() -> {
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
    public Single<ResultSet> findByGroupId(long groupId) {
        return Db.singleResultSet(() -> {
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
    public Single<Long> deleteById(Long id) {
        return Db.singleLong(() -> {
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
    public Single<Long> deleteByGroupId(long id) {
        return Db.singleLong(() -> {
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
    public Single<Long> deleteByTargetId(long id) {
        return Db.singleLong(() -> {
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
