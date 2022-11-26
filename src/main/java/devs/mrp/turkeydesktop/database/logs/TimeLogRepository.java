/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.type.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Single;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author miguel
 */
public class TimeLogRepository implements TimeLogDao {
    
    private final Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(TimeLogRepository.class.getName());
    
    private static TimeLogRepository instance;
    
    private static final GenericCache<Long,TimeLog> cache = new GenericCacheImpl<>();
    
    private TimeLogRepository(){
        
    }
    
    static TimeLogRepository getInstance() {
        if (instance == null) {
            instance = new TimeLogRepository();
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(TimeLog element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) ", 
                        Db.WATCHDOG_TABLE,
                        TimeLog.EPOCH, // 1
                        TimeLog.ELAPSED, // 2
                        TimeLog.COUNTED, // 3
                        TimeLog.ACCUMULATED, // 4
                        TimeLog.PID, // 5
                        TimeLog.PROCESS_NAME, // 6
                        TimeLog.WINDOW_TITLE, // 7
                        Group.GROUP, // 8
                        Type.TYPE) // 9
                        + "VALUES (?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                stm.setLong(1, element.getEpoch());
                stm.setLong(2, element.getElapsed());
                stm.setLong(3, element.getCounted());
                stm.setLong(4, element.getAccumulated());
                stm.setString(5, element.getPid());
                stm.setString(6, element.getProcessName());
                stm.setString(7, element.getWindowTitle());
                stm.setLong(8, element.getGroupId());
                if (element.getType() != null) {
                    stm.setString(9, element.getType().toString());
                }
                stm.executeUpdate();
                ResultSet generatedId = stm.getGeneratedKeys();
                if (generatedId.next()) {
                    result = generatedId.getLong(1);
                }
                cache.put(element.getId(), element);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "SQL exception", ex);
            }
            return result;
        });
    }

    @Override
    public Single<Long> update(TimeLog element) {
        return Db.singleLong(() -> {
            long entriesUpdated = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
                        Db.WATCHDOG_TABLE, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.COUNTED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE, Group.GROUP, Type.TYPE, TimeLog.ID));
                stm.setLong(1, element.getEpoch());
                stm.setLong(2, element.getElapsed());
                stm.setLong(3, element.getCounted());
                stm.setString(4, element.getPid());
                stm.setString(5, element.getProcessName());
                stm.setString(6, element.getWindowTitle());
                stm.setLong(7, element.getId());
                stm.setLong(8, element.getGroupId());
                stm.setString(9, element.getType().toString());
                entriesUpdated = stm.executeUpdate();
                cache.put(element.getId(), element);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return entriesUpdated;
        });
    }

    @Override
    public Single<ResultSet> findAll() {
        return Db.singleResultSet(() -> {
        ResultSet rs = null;
            PreparedStatement stm;
            try {
                // get from last 24 hours only by default to not overload memory
                LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusHours(24);
                ZonedDateTime zdt = ZonedDateTime.of(now, ZoneId.systemDefault());
                zdt.toInstant().toEpochMilli();
                long frame = zdt.toInstant().toEpochMilli();
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s>?",
                        Db.WATCHDOG_TABLE, TimeLog.EPOCH));
                stm.setLong(1, frame);
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
                        Db.WATCHDOG_TABLE, TimeLog.ID));
                stm.setLong(1, id);
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
                        Db.WATCHDOG_TABLE, TimeLog.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
        
    }

    @Override
    public Single<ResultSet> getTimeFrameGroupedByProcess(long from, long to) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT %s, SUM(%s) FROM %s WHERE %s>=? AND %s<=? GROUP BY %s",
                        TimeLog.PROCESS_NAME, TimeLog.ELAPSED, Db.WATCHDOG_TABLE, TimeLog.EPOCH, TimeLog.EPOCH, TimeLog.PROCESS_NAME));
                stm.setLong(1, from);
                stm.setLong(2, to);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }
    
    @Override
    public Single<ResultSet> getTimeFrameOfGroup(long groupId, long from, long to) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT %s, SUM(%s) FROM %s WHERE %s>=? AND %s<=? AND %s=? GROUP BY %s",
                        Group.GROUP, TimeLog.ELAPSED, Db.WATCHDOG_TABLE, TimeLog.EPOCH, TimeLog.EPOCH, Group.GROUP, Group.GROUP));
                stm.setLong(1, from);
                stm.setLong(2, to);
                stm.setLong(3, groupId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> getMostRecent() {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 1",
                        Db.WATCHDOG_TABLE, TimeLog.EPOCH));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> getGroupedByTitle(long from, long to) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT %s, SUM(%s) FROM %s WHERE %s>=? AND %s<=? GROUP BY %s",
                        TimeLog.WINDOW_TITLE,
                        TimeLog.ELAPSED,
                        Db.WATCHDOG_TABLE,
                        TimeLog.EPOCH,
                        TimeLog.EPOCH, 
                        TimeLog.WINDOW_TITLE));
                stm.setLong(1, from);
                stm.setLong(2, to);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }
    
}
