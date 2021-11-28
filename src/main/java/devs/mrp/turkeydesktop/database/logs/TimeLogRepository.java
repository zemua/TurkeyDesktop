/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class TimeLogRepository implements TimeLogDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(TimeLogRepository.class.getName());
    private Semaphore semaphore = new Semaphore(1);
    
    private static TimeLogRepository instance;
    
    private TimeLogRepository(){
        
    }
    
    public static TimeLogRepository getInstance() {
        if (instance == null) {
            instance = new TimeLogRepository();
        }
        return instance;
    }
    
    @Override
    public long add(TimeLog element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) ", 
                        Db.WATCHDOG_TABLE, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE)
                        + "VALUES (?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                stm.setLong(1, element.getEpoch());
                stm.setLong(2, element.getElapsed());
                stm.setString(3, element.getPid());
                stm.setString(4, element.getProcessName());
                stm.setString(5, element.getWindowTitle());
                stm.executeUpdate();
                ResultSet generatedId = stm.getGeneratedKeys();
                if (generatedId.next()) {
                    result = generatedId.getLong(1);
                }
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
    public long update(TimeLog element) {
        long entriesUpdated = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
                        Db.WATCHDOG_TABLE, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE, TimeLog.ID));
                stm.setLong(1, element.getEpoch());
                stm.setLong(2, element.getElapsed());
                stm.setString(3, element.getPid());
                stm.setString(4, element.getProcessName());
                stm.setString(5, element.getWindowTitle());
                stm.setLong(6, element.getId());
                entriesUpdated = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return entriesUpdated;
    }

    @Override
    public ResultSet findAll() {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                // get from last 24 hours only by default to not overload memory
                long frame = System.currentTimeMillis() - (24*60*60*1000);
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s>?",
                        Db.WATCHDOG_TABLE, TimeLog.EPOCH));
                stm.setLong(1, frame);
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
    public ResultSet findById(long id) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.WATCHDOG_TABLE, TimeLog.ID));
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
    public long deleteById(long id) {
        long delQty = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.WATCHDOG_TABLE, TimeLog.ID));
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

    @Override
    public ResultSet getTimeFrameGroupedByProcess(long from, long to) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format(String.format("SELECT %s, SUM(%s) FROM %s WHERE %s>=? AND %s<=? GROUP BY %s",
                        TimeLog.PROCESS_NAME, TimeLog.ELAPSED, Db.WATCHDOG_TABLE, TimeLog.EPOCH, TimeLog.EPOCH, TimeLog.PROCESS_NAME)));
                stm.setLong(1, from);
                stm.setLong(2, to);
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
    
}