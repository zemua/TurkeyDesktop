/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

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
public class GroupRepository implements GroupDao {
    
    private Db dbInstance = Db.getInstance();
    private static final Logger logger = Logger.getLogger(GroupRepository.class.getName());
    
    private static GroupRepository instance;
    
    private GroupRepository() {
        
    }
    
    public static GroupRepository getInstance() {
        if (instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }
    
    @Override
    public Observable<Long> add(Group element) {
        return Db.observableLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s) ",
                        Db.GROUPS_TABLE, Group.NAME, Group.TYPE)
                        + "VALUES (?, ?)");
                stm.setString(1, element.getName());
                stm.setString(2, element.getType().toString());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Observable<Long> update(Group element) {
        return Db.observableLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.GROUPS_TABLE, Group.NAME, Group.TYPE, Group.PREVENT_CLOSE, Group.ID));
                stm.setString(1, element.getName());
                stm.setString(2, element.getType().toString());
                stm.setBoolean(3, element.isPreventClose());
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
                            Db.GROUPS_TABLE));
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
                        Db.GROUPS_TABLE, Group.ID));
                stm.setLong(1, id);
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
                        Db.GROUPS_TABLE, Group.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }

    @Override
    public Observable<ResultSet> findAllOfType(Group.GroupType type) {
        return Db.observableResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUPS_TABLE, Group.TYPE));
                stm.setString(1, type.toString());
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }
    
    @Override
    public Observable<Integer> setPreventClose(long groupId, boolean preventClose) {
        return Db.observableInt(() -> {
            int affectedRows = 0;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                        Db.GROUPS_TABLE, Group.PREVENT_CLOSE, Group.ID));
                stm.setBoolean(1, preventClose);
                stm.setLong(2, groupId);
                affectedRows = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return affectedRows;
        });
    }
    
}
