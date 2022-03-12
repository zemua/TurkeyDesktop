/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Semaphore;
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
    public static final String GROUP_ASSIGNATION_TABLE = "GROUP_ASSIGNATION_TABLE";
    public static final String CATEGORIZED_TABLE = "TYPES_CATEGORIZATION";
    public static final String CONDITIONS_TABLE = "CONDITIONS_TABLE";
    public static final String TITLES_TABLE = "TITLES_TABLE";
    public static final String ACCUMULATED_TIME_TABLE = "ACCUMULATED_TIME";
    public static final String CONFIG_TABLE = "CONFIG_TABLE";
    private static final Semaphore semaphore = new Semaphore(1);
    
    private static Db instance = null;
    private Connection con = null;
    
    private Db(){
        
    }
    
    public static Db getInstance(){
        if (instance == null) {
            instance = new Db();
            instance.inicializar();
        }
        return instance;
    }
    
    public static Semaphore getSemaphore() {
        return semaphore;
    }
    
    private void setConnection(){
        try {
            if (con != null && !con.isClosed()) return;
            con = DriverManager.getConnection("jdbc:h2:" + DbFiles.getDbFilePath());
        } catch (SQLException ex) {
            System.out.println("error intentando conseguir la conexión");
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            con = null;
            JOptionPane.showMessageDialog(null, "Error: Is it possible that the application is already opened?");
            WatchDog.getInstance().stop();
        }
    }
    
    private void inicializar(){
        setConnection();
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s("
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s BIGINT NOT NULL, " // epoch
                + "%s INT NOT NULL, " // elapsed
                + "%s INT NOT NULL, " // counted
                + "%s BIGINT NOT NULL, " // accumulated
                + "%s VARCHAR(10), " // pid
                + "%s VARCHAR(50), " // process name
                + "%s VARCHAR(150), " // window title
                + "%s INT, " // group id
                + "%s VARCHAR(15), " // type id
                + "PRIMARY KEY (%s))",
                WATCHDOG_TABLE, TimeLog.ID, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.COUNTED, TimeLog.ACCUMULATED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE, Group.GROUP, Type.TYPE, TimeLog.ID));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s("
                + "%s VARCHAR(50) NOT NULL, " // process name, unique in the table
                + "%s VARCHAR(50), " // TYPE
                + "PRIMARY KEY (%s))",
                CATEGORIZED_TABLE, Type.PROCESS_NAME, Type.TYPE, Type.PROCESS_NAME));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s VARCHAR(50), " // group name
                + "%s VARCHAR(50), " // type
                + "PRIMARY KEY (%s))",
                GROUPS_TABLE, Group.ID, Group.NAME, Group.TYPE, Group.ID));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s VARCHAR(150) NOT NULL, " // key
                + "%s VARCHAR(150) NOT NULL, " // value
                + "PRIMARY KEY (%s))",
                    CONFIG_TABLE, ConfigElement.KEY, ConfigElement.VALUE, ConfigElement.KEY));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s VARCHAR(300) NOT NULL, " // the string to match, unique
                + "%s VARCHAR(15) NOT NULL, " // whether it is positive or negative
                + "PRIMARY KEY (%s))",
                TITLES_TABLE, Title.SUB_STR, Title.TYPE, Title.SUB_STR));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s VARCHAR(15) NOT NULL, " // the element type either process or title
                + "%s VARCHAR(300) NOT NULL, " // the id of the element be it process name or title substring
                + "%s BIGINT NOT NULL, " // the id of the group
                + "PRIMARY KEY (%s))", // type + element id as primary key
                GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID, GroupAssignation.GROUP_ID, GroupAssignation.TYPE + "," + GroupAssignation.ELEMENT_ID));
        
        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s BIGINT NOT NULL, " // the group id to which this condition belongs
                + "%s BIGINT NOT NULL, " // target id, like the other group's id, the randome check id, etc.
                + "%s BIGINT NOT NULL, " // usage time from the target that has to be met
                + "%s INT NOT NULL, " // timeframe in days for the usage time to be met
                + "PRIMARY KEY (%s))",
                CONDITIONS_TABLE, Condition.ID, Condition.GROUP_ID, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION, Condition.ID));
        
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
