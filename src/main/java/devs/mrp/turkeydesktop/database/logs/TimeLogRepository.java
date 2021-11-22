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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class TimeLogRepository implements TimeLogDao {
    
    Db dbInstance = Db.getInstance();
    Logger logger = Logger.getLogger(TimeLogRepository.class.getName());
    
    @Override
    public long add(TimeLog element) {
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
            return -1;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    @Override
    public long update(TimeLog element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet findById() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long deleteById() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
