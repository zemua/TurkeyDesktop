/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
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
public class TitledLogRepoFacade implements ITitledLogDaoFacade {
    
    private final Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(TitledLogRepoFacade.class.getName());
    private Semaphore semaphore = new Semaphore(1);
    
    private static TitledLogRepoFacade instance;
    
    private TitledLogRepoFacade() {
        
    }
    
    public static TitledLogRepoFacade getInstance() {
        if (instance == null) {
            instance = new TitledLogRepoFacade();
        }
        return instance;
    }
    
    @Override
    public ResultSet getTimeFrameOfDependablesGroupedByProcess(long from, long to) {
        ResultSet rs = null;
        try {
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT %s, SUM(%s) FROM %s LEFT JOIN %s ON %s WHERE %s>=? AND %s<=? AND %s=? GROUP BY %s",
                        TimeLog.PROCESS_NAME, 
                        TimeLog.ELAPSED, 
                        Db.WATCHDOG_TABLE, 
                        Db.CATEGORIZED_TABLE, 
                        Db.WATCHDOG_TABLE + "." + TimeLog.PROCESS_NAME + "=" + Db.CATEGORIZED_TABLE + "." + Type.PROCESS_NAME, 
                        TimeLog.EPOCH, 
                        TimeLog.EPOCH, 
                        Db.CATEGORIZED_TABLE + "." + Type.TYPE,
                        TimeLog.PROCESS_NAME));
                stm.setLong(1, from);
                stm.setLong(2, to);
                stm.setString(3, Type.Types.DEPENDS.toString());
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
