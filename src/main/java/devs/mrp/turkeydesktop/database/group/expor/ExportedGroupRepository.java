/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class ExportedGroupRepository implements ExportedGroupDao {
    
    private Db dbInstance = Db.getInstance();
    private static final Logger logger = Logger.getLogger(ExportedGroupRepository.class.getName());
    
    private static ExportedGroupRepository instance;
    
    private ExportedGroupRepository() {
        
    }
    
    public static ExportedGroupRepository getInstance() {
        if (instance == null) {
            instance = new ExportedGroupRepository();
        }
        return instance;
    }
    
    @Override
    public Single<ResultSet> findByGroup(Long id) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP));
                stm.setLong(1, id);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
        
    }

    @Override
    public Single<ResultSet> findByGroupAndFile(Long groupId, String file) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE));
                stm.setLong(1, groupId);
                stm.setString(2, file);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> deleteByGroup(Long id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }

    @Override
    public Single<Long> add(ExportedGroup element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s) ",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE, ExportedGroup.DAYS)
                        + "VALUES (?, ?, ?)");
                stm.setLong(1, element.getGroup());
                stm.setString(2, element.getFile());
                stm.setLong(3, element.getDays());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Single<Long> update(ExportedGroup element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=? WHERE %s=? ",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.FILE, ExportedGroup.DAYS, ExportedGroup.GROUP));
                stm.setString(1, element.getFile());
                stm.setLong(2, element.getDays());
                stm.setLong(3, element.getGroup());
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
                        Db.GROUPS_EXPORT_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(ExportedGroupId id) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE));
                stm.setLong(1, id.getGroup());
                stm.setString(2, id.getFile());
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> deleteById(ExportedGroupId id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=? AND %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE));
                stm.setLong(1, id.getGroup());
                stm.setString(2, id.getFile());
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
}
