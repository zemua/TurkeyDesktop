/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ConfigElementRepository implements ConfigElementDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(ConfigElementRepository.class.getName());
    private Semaphore semaphore = new Semaphore(1);
    
    private static ConfigElementRepository instance;
    
    private ConfigElementRepository() {
        
    }
    
    public static ConfigElementRepository getInstance() {
        if (instance == null) {
            instance = new ConfigElementRepository();
        }
        return instance;
    }
    
    @Override
    public long add(ConfigElement element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s) ",
                        Db.CONFIG_TABLE, ConfigElement.KEY, ConfigElement.VALUE)
                        + "VALUES (?, ?)");
                stm.setString(1, element.getKey().toString());
                stm.setString(2, element.getValue());
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
    public long update(ConfigElement element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=? WHERE %s=? ",
                        Db.CONFIG_TABLE, ConfigElement.VALUE, ConfigElement.KEY)
                        + "VALUES (?, ?)");
                stm.setString(1, element.getValue());
                stm.setString(2, element.getKey().toString());
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
                        Db.CONFIG_TABLE));
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
    public ResultSet findById(String id) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CONFIG_TABLE, ConfigElement.KEY));
                stm.setString(1, id);
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
    public long deleteById(String id) {
        long delQty = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONFIG_TABLE, ConfigElement.KEY));
                stm.setString(1, id);
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
