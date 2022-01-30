/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.type.Type;
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
public abstract class AssignableElementRepository implements AssignableElementDao {
    
    /**
     * Cannot make it without introducing a bug or duplicating GroupAssignationCode
     * as one element id can have the same value if process and title have the same name
     * so we do the mapping in the service without using SQL Joins
     */
    
    /*private Db dbInstance = Db.getInstance();
    private static final Logger logger = Logger.getLogger(AssignableElementRepository.class.getName());
    private final Semaphore semaphore = Db.getSemaphore();
    
    private static AssignableElementRepository instance;
    
    private AssignableElementRepository() {
        
    }
    
    public static AssignableElementRepository getInstance() {
        if (instance == null) {
            instance = new AssignableElementRepository();
        }
        return instance;
    }

    @Override
    public ResultSet findPositiveProcessesWithAssignation() {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT %s, %s, %s, %s FROM %s LEFT JOIN %s ON %s WHERE %s=? AND (%s=? OR %s=?)",
                        Db.CATEGORIZED_TABLE + "." + Type.PROCESS_NAME,
                        Db.GROUP_ASSIGNATION_TABLE + "." + GroupAssignation.TYPE,
                        Db.GROUP_ASSIGNATION_TABLE + "." + GroupAssignation.GROUP_ID,
                        Db.GROUP_ASSIGNATION_TABLE + "." + GroupAssignation.ID,
                        Db.CATEGORIZED_TABLE,
                        Db.GROUP_ASSIGNATION_TABLE,
                        Db.CATEGORIZED_TABLE + "." + Type.PROCESS_NAME + "=" + Db.GROUP_ASSIGNATION_TABLE + "." + GroupAssignation.ELEMENT_ID,
                        Db.CATEGORIZED_TABLE + "." + Type.TYPE,
                        Db.GROUP_ASSIGNATION_TABLE + "." + GroupAssignation.TYPE,
                        Db.GROUP_ASSIGNATION_TABLE + "." + GroupAssignation.TYPE));
                stm.setString(1, Type.Types.POSITIVE.toString());
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

    @Override
    public ResultSet findNegativeProcessesWithAssignation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet findPositiveTitlesWithAssignation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet findNegativeTitlesWithAssignation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
}
