package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogRepository;
import devs.mrp.turkeydesktop.database.type.Type;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAndTypeFacadeRepository implements LogAndTypeFacadeDao {
    
    private Db dbInstance;
    private Logger logger = Logger.getLogger(TimeLogRepository.class.getName());
    
    private static LogAndTypeFacadeRepository instance;
    
    private LogAndTypeFacadeRepository(LogAndTypeFacadeFactory factory) {
        this.dbInstance = factory.getDb();
    }
    
    static LogAndTypeFacadeRepository getInstance(LogAndTypeFacadeFactory factory) {
        if (instance == null) {
            instance = new LogAndTypeFacadeRepository(factory);
        }
        return instance;
    }
    
    @Override
    public Single<ResultSet> getTypedLogGroupedByProcess(long from, long to) {
        return dbInstance.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT %s, %s, SUM(%s) FROM %s LEFT JOIN %s ON %s WHERE %s>=? AND %s<=? GROUP BY %s",
                        Db.WATCHDOG_TABLE + "." + TimeLog.PROCESS_NAME,
                        Db.CATEGORIZED_TABLE + "." + Type.TYPE,
                        TimeLog.ELAPSED,
                        Db.WATCHDOG_TABLE,
                        Db.CATEGORIZED_TABLE,
                        Db.WATCHDOG_TABLE + "." + TimeLog.PROCESS_NAME + "=" + Db.CATEGORIZED_TABLE + "." + Type.PROCESS_NAME,
                        TimeLog.EPOCH,
                        TimeLog.EPOCH, 
                        Db.WATCHDOG_TABLE + "." + TimeLog.PROCESS_NAME));
                stm.setLong(1, from);
                stm.setLong(2, to);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }
    
}
