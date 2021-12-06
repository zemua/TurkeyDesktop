/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.database.category.Group;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author miguel
 */
public class Db { // TODO create asynchronous listeners to update livedata
    
    public static final String WATCHDOG_TABLE = "WATCHDOG_LOG";
    public static final String GROUPS_TABLE = "GROUPS_OF_APPS";
    
    private static Db instance = null;
    private Connection con = null;
    
    private Db(){
        
    }
    
    public static Db getInstance(){
        if (instance == null) {
            instance = new Db();
            instance.inicializar();
            return instance;
        }
        else{
            return instance;
        }
    }
    
    private void setConnection(){
        try {
            if (con != null && !con.isClosed()) return;
            con = DriverManager.getConnection("jdbc:h2:" + DbFiles.getDbFilePath());
        } catch (SQLException ex) {
            System.out.println("error intentando conseguir la conexión");
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            con = null;
            JOptionPane.showMessageDialog(null, "Error: Es posible que ya tengas otra ventana abierta?");
        }
    }
    
    private void inicializar(){
        setConnection();
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s("
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s BIGINT NOT NULL, " // epoch
                + "%s INT NOT NULL, " // elapsed
                + "%s VARCHAR(10), " // pid
                + "%s VARCHAR(50), " // process name
                + "%s VARCHAR(150), " // window title
                + "%s INT, " // category id
                + "%s INT, " // type id
                + "PRIMARY KEY (%s))",
                WATCHDOG_TABLE, TimeLog.ID, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE, Group.GROUP, Type.TYPE, TimeLog.ID));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s("
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s VARCHAR(50), " // type name
                + "PRIMARY KEY (%s))",
                GROUPS_TABLE, Group.ID, Group.NAME, Group.TYPE, Group.ID));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s("
                + "%s BIGINT NOT NULL AUTO_INCREMENT, "
                + "%s VARCHAR(50), "
                + "%s VARCHAR(50), "
                + "PRIMARY KEY (%s))",
                GROUPS_TABLE, Group.ID, Group.NAME, Group.TYPE, Group.ID));
        
        
        //close();
    }
    
    public Connection getConnection(){
        //setConnection();
        return con;
    }
    
    private void close(){
        try {
            if (con.isClosed())return;
            con.commit();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ResultSet getQuery(String str){
        //setConnection();
        ResultSet rs = null;
        try {
            Statement stm = con.createStatement();
            rs = stm.executeQuery(str);
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        //close();
        return rs;
    }
    
    private boolean execute(String str){
        //setConnection();
        boolean rs = false;
        try {
            Statement stm = con.createStatement();
            rs = stm.execute(str);
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        //close();
        return rs;
    }
    
    private int executeUpdate(String str){
        //setConnection();
        int rs = 0;
        try {
            Statement stm = con.createStatement();
            rs = stm.executeUpdate(str);
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
        //close();
        return rs;
    }
}
