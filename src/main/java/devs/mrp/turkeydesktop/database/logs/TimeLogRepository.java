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
    
    Db dbInstance = Db.getInstance();
    Logger logger = Logger.getLogger(TimeLogRepository.class.getName());
    Semaphore semaphore = new Semaphore(1);
    
    // TODO make singleton
    
    @Override
    public long add(TimeLog element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement("INSERT INTO WATCHDOG_LOG (EPOCH, ELAPSED, PID, PROCESS_NAME, WINDOW_TITLE) "
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
                stm = dbInstance.getConnection().prepareStatement("UPDATE WATCHDOG_LOG SET EPOCH=?, ELAPSED=?, PID=?, PROCESS_NAME=?, WINDOW_TITLE=? WHERE ID=?");
                stm.setLong(1, element.getEpoch());
                stm.setLong(2, element.getElapsed());
                stm.setString(3, element.getPid());
                stm.setString(4, element.getProcessName());
                stm.setString(5, element.getWindowTitle());
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
                stm = dbInstance.getConnection().prepareStatement("SELECT * FROM WATCHDOG_LOG WHERE EPOCH>?");
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
                stm = dbInstance.getConnection().prepareStatement("SELECT * FROM WATCHDOG_LOG WHERE ID=?");
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
                stm = dbInstance.getConnection().prepareStatement("DELETE FROM WATCHDOG_LOG WHERE ID=?");
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
