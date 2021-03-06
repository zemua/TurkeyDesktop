/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.Group;
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
public class GroupAssignationRepository implements GroupAssignationDao {
    
    private Db dbInstance = Db.getInstance();
    private static final Logger logger = Logger.getLogger(GroupAssignationRepository.class.getName());
    private final Semaphore semaphore = Db.getSemaphore();
    
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
    public ResultSet findByElementId(GroupAssignation.ElementType elementType, String elementId) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
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
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return rs;
    }

    @Override
    public ResultSet findAllElementTypeOfGroup(GroupAssignation.ElementType elementType, Long groupId) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
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
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return rs;
    }

    @Override
    public long add(GroupAssignation element) {
        long result = -1;
        try {
            semaphore.acquire();
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
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return result;
    }

    @Override
    public long update(GroupAssignation element) {
        long result = -1;
        try {
            semaphore.acquire();
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
                        Db.GROUP_ASSIGNATION_TABLE));
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

    @Deprecated
    @Override
    public ResultSet findById(Long id) {
        return null;
        /*ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.ID));
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
        return rs;*/
    }

    @Deprecated
    @Override
    public long deleteById(Long id) {
        return 0;
        /*
        long delQty = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.ID));
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
        return delQty;*/
    }
    
    @Override
    public long deleteByElementId(GroupAssignation.ElementType elementType, String elementId) {
        long delQty = -1;
        try {
            semaphore.acquire();
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
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return delQty;
    }
    
    @Override
    public ResultSet findAllOfType(GroupAssignation.ElementType elementType) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE));
                stm.setString(1, elementType.toString());
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
    public long deleteByGroupId(long groupId) {
        long delQty = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.GROUP_ID));
                stm.setLong(1, groupId);
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
