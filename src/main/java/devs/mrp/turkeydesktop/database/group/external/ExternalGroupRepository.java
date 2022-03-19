/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.database.Db;
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
public class ExternalGroupRepository implements ExternalGroupDao {
    
    private Db dbInstance = Db.getInstance();
    private final Semaphore semaphore = Db.getSemaphore();
    private static final Logger logger = Logger.getLogger(ExternalGroupRepository.class.getName());
    
    private static ExternalGroupRepository instance;
    
    private ExternalGroupRepository() {
        
    }
    
    public static ExternalGroupRepository getInstance() {
        if (instance == null) {
            instance = new ExternalGroupRepository();
        }
        return instance;
    }
    
    @Override
    public long add(ExternalGroup element) {
        long result = -1;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s) ",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.GROUP, ExternalGroup.FILE)
                        + "VALUES (?, ?)");
                stm.setLong(1, element.getGroup());
                stm.setString(2, element.getFile());
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
    public long update(ExternalGroup element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
