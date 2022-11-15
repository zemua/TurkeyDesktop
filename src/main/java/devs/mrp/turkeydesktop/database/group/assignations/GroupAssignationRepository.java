/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

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
public class GroupAssignationRepository implements GroupAssignationDao {
    
    private Db dbInstance = Db.getInstance();
    private static final Logger logger = Logger.getLogger(GroupAssignationRepository.class.getName());
    
    private static GroupAssignationRepository instance;
    
    private GroupAssignationRepository() {
        
    }
    
    public static GroupAssignationRepository getInstance() {
        if (instance == null) {
            instance = new GroupAssignationRepository();
        }
        return instance;
    }
    
    @Override
    public Single<ResultSet> findByElementId(GroupAssignation.ElementType elementType, String elementId) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID));
                stm.setString(1, elementType.toString());
                stm.setString(2, elementId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findAllElementTypeOfGroup(GroupAssignation.ElementType elementType, Long groupId) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.GROUP_ID));
                stm.setString(1, elementType.toString());
                stm.setLong(2, groupId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> add(GroupAssignation element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s, %s) ",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID, GroupAssignation.GROUP_ID)
                        + "VALUES (?, ?, ?)");
                stm.setString(1, element.getType().toString());
                stm.setString(2, element.getElementId());
                stm.setLong(3, element.getGroupId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Single<Long> update(GroupAssignation element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID, GroupAssignation.GROUP_ID));
                stm.setString(1, element.getType().toString());
                stm.setString(2, element.getElementId());
                stm.setLong(3, element.getGroupId());
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
                        Db.GROUP_ASSIGNATION_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }

    @Deprecated
    @Override
    public Single<ResultSet> findById(GroupAssignationDao.ElementId id) {
        return Single.just(null);
    }

    @Deprecated
    @Override
    public Single<Long> deleteById(GroupAssignationDao.ElementId id) {
        return Single.just(0L);
    }
    
    @Override
    public Single<Long> deleteByElementId(GroupAssignation.ElementType elementType, String elementId) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=? AND %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID));
                stm.setString(1, elementType.toString());
                stm.setString(2, elementId);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
        
    }
    
    @Override
    public Single<ResultSet> findAllOfType(GroupAssignation.ElementType elementType) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE));
                stm.setString(1, elementType.toString());
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> deleteByGroupId(long groupId) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.GROUP_ID));
                stm.setLong(1, groupId);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
}
