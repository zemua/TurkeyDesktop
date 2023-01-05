/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.closeables.Closeable;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class Db { // TODO create asynchronous listeners to update livedata

    public static final String WATCHDOG_TABLE = "WATCHDOG_LOG";
    public static final String GROUPS_TABLE = "GROUPS_OF_APPS";
    public static final String GROUPS_EXTERNAL_TABLE = "GROUPS_EXTERNAL_TABLE";
    public static final String GROUPS_EXPORT_TABLE = "GROUPS_EXPORT_TABLE";
    public static final String GROUP_ASSIGNATION_TABLE = "GROUP_ASSIGNATION_TABLE";
    public static final String CATEGORIZED_TABLE = "TYPES_CATEGORIZATION";
    public static final String CLOSEABLES_TABLE = "CLOSEABLES_TABLE";
    public static final String CONDITIONS_TABLE = "CONDITIONS_TABLE";
    public static final String TITLES_TABLE = "TITLES_TABLE";
    public static final String ACCUMULATED_TIME_TABLE = "ACCUMULATED_TIME";
    public static final String CONFIG_TABLE = "CONFIG_TABLE";
    public static final String IMPORTS_TABLE = "IMPORTS_TABLE";

    private static final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private static Db instance = null;
    private static Connection con = null;
    
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    
    private Db() {
    }

    public static Db getInstance() {
        if (instance == null) {
            instance = new Db();
            instance.inicializar();
        }
        return instance;
    }
    
    public static Single<String> singleString(Callable<String> callable) {
        log.debug("Creating singleString from {}", Arrays.toString(Thread.currentThread().getStackTrace()));
        return Single.defer(() -> Single.fromCallable(callable)).subscribeOn(Schedulers.from(dbExecutor)).observeOn(Schedulers.computation());
    }
    
    public static Single<Long> singleLong(Callable<Long> callable) {
        log.debug("Creating singleLong from {}", Arrays.toString(Thread.currentThread().getStackTrace()));
        return Single.defer(() -> Single.fromCallable(callable)).subscribeOn(Schedulers.from(dbExecutor)).observeOn(Schedulers.computation());
    }
    
    public static Single<Integer> singleInt(Callable<Integer> callable) {
        log.debug("Creating singleInt from {}", Arrays.toString(Thread.currentThread().getStackTrace()));
        return Single.defer(() -> Single.fromCallable(callable)).subscribeOn(Schedulers.from(dbExecutor)).observeOn(Schedulers.computation());
    }
    
    public static Single<Boolean> singleBoolean(Callable<Boolean> callable) {
        log.debug("Creating singleBoolean from {}", Arrays.toString(Thread.currentThread().getStackTrace()));
        return Single.defer(() -> Single.fromCallable(callable)).subscribeOn(Schedulers.from(dbExecutor)).observeOn(Schedulers.computation());
    }
    
    public static Single<ResultSet> singleResultSet(Callable<ResultSet> callable) {
        log.debug("Creating singleResultSet from {}", Arrays.toString(Thread.currentThread().getStackTrace()));
        return Single.defer(() -> Single.fromCallable(callable)).subscribeOn(Schedulers.from(dbExecutor)).observeOn(Schedulers.computation());
    }
    
    public static <T> Single<T> singleGeneric(Callable<T> callable) {
        log.debug("Creating singleGeneric from {}", Arrays.toString(Thread.currentThread().getStackTrace()));
        return Single.defer(() -> Single.fromCallable(callable)).subscribeOn(Schedulers.from(dbExecutor)).observeOn(Schedulers.computation());
    }

    public boolean verifyCanGetDb() {
        try {
            if (Objects.nonNull(con) && !con.isClosed()) {
                return true;
            }
            con = DriverManager.getConnection("jdbc:h2:" + DbFiles.getDbFilePath());
            return Objects.nonNull(con) && !con.isClosed();
        } catch (SQLException ex) {
            log.error("error trying to stablish db connection", ex);
            try {
                if(con != null) {
                    con.close();
                }
            } catch (SQLException ex1) {
                log.error("error trying to close db connection", ex1);
            }
        }
        return false;
    }

    private void setConnection() {
        try {
            if (con != null && !con.isClosed()) {
                return;
            }
            log.warn("Connection was not established, setting it up. Connection is: {}", con);
            con = DriverManager.getConnection("jdbc:h2:" + DbFiles.getDbFilePath());
        } catch (SQLException ex) {
            System.out.println("error trying to get DB connection");
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            con = null;
            WatchDogImpl.getInstance().stop();
            JOptionPane.showMessageDialog(null, localeMessages.getString("anotherConnOpen"));
            System.exit(0);
        }
    }

    public boolean isConnectionNull() {
        return Objects.isNull(con);
    }

    private void inicializar() {
        setConnection();

        execute(String.format("CREATE TABLE IF NOT EXISTS %s("
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s BIGINT NOT NULL, " // epoch
                + "%s INT NOT NULL, " // elapsed
                + "%s INT NOT NULL, " // counted
                + "%s BIGINT NOT NULL, " // accumulated
                + "%s VARCHAR(10), " // pid
                + "%s VARCHAR(50), " // process name
                + "%s VARCHAR(500), " // window title
                + "%s INT, " // group id
                + "%s VARCHAR(15), " // type id
                + "PRIMARY KEY (%s))",
                WATCHDOG_TABLE, TimeLog.ID, TimeLog.EPOCH, TimeLog.ELAPSED, TimeLog.COUNTED, TimeLog.ACCUMULATED, TimeLog.PID, TimeLog.PROCESS_NAME, TimeLog.WINDOW_TITLE, Group.GROUP, Type.TYPE, TimeLog.ID));
        
        execute(String.format("CREATE UNIQUE INDEX IF NOT EXISTS %s ON %s(%s)",
                "EPOCH_INDEX", WATCHDOG_TABLE, TimeLog.EPOCH));

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

        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s VARCHAR(500) NOT NULL," // file path
                + "PRIMARY KEY (%s))",
                IMPORTS_TABLE, ConfigurationEnum.IMPORT_PATH.toString(), ConfigurationEnum.IMPORT_PATH.toString()));

        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s BIGINT NOT NULL AUTO_INCREMENT, " // id
                + "%s BIGINT NOT NULL, " // the group id to which this external time belongs
                + "%s VARCHAR(500) NOT NULL," // file path with date-time data
                + "PRIMARY KEY (%s))",
                GROUPS_EXTERNAL_TABLE, ExternalGroup.ID, ExternalGroup.GROUP, ExternalGroup.FILE, ExternalGroup.ID));

        // only one per group max
        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s BIGINT NOT NULL, " // the group id to which this external time belongs
                + "%s VARCHAR(500) NOT NULL, " // file path with date-time data
                + "%s INT NOT NULL, " // days to be included in the export
                + "PRIMARY KEY (%s))",
                GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE, ExportedGroup.DAYS, ExportedGroup.GROUP));

        execute(String.format("CREATE TABLE IF NOT EXISTS %s(" // table name
                + "%s VARCHAR(50), " // process name
                + "PRIMARY KEY (%s))",
                CLOSEABLES_TABLE, Closeable.PROCESS_NAME, Closeable.PROCESS_NAME));
        
        // add closeable boolean to group table
        execute(String.format("ALTER TABLE %s " // table name
                + "ADD COLUMN IF NOT EXISTS %s BOOLEAN DEFAULT FALSE", // id
                GROUPS_TABLE, Group.PREVENT_CLOSE));
        
        // REMOVE OLD LOG ENTRIES THAT ARE OF NO USE
        execute(String.format("DELETE FROM %s WHERE %s < %s",
                WATCHDOG_TABLE,
                TimeLog.EPOCH,
                TimeConverter.beginningOfOffsetDays(30)));
        
        //close();
    }

    public Connection getConnection() {
        setConnection();
        return con;
    }
    
    public PreparedStatement prepareStatement(String statement) throws SQLException {
        return con.prepareStatement(statement);
    }
    
    public PreparedStatement prepareStatementWithGeneratedKeys(String statement) throws SQLException {
        return con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
    }

    private void close() {
        try {
            if (con.isClosed()) {
                return;
            }
            con.commit();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ResultSet getQuery(String str) {
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

    private boolean execute(String str) {
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

    private int executeUpdate(String str) {
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
