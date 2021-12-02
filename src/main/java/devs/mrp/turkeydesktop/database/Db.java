/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.database.category.Category;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
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
public class Db {
    
    public static final String WATCHDOG_TABLE = "WATCHDOG_LOG";
    
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
                + "%s BIGINT NOT NULL AUTO_INCREMENT, "
                + "%s BIGINT NOT NULL, "
                + "%s INT NOT NULL, "
                + "%s VARCHAR(10), "
                + "%s VARCHAR(50), "
                + "%s VARCHAR(150), "
                + "%s VARCHAR(15), "
                + "PRIMARY KEY (%s))",
                WATCHDOG_TABLE, TimeLog.ID, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE, Category.CATEGORY, TimeLog.ID));
        
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
