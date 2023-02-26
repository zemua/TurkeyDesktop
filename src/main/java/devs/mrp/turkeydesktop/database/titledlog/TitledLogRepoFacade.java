package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TitledLogRepoFacade implements TitledLogDaoFacade {
    
    private final Db db;
    private Logger logger = Logger.getLogger(TitledLogRepoFacade.class.getName());
    
    public TitledLogRepoFacade(TitledLogFacadeFactory factory) {
        db = factory.getDb();
    }
    
    @Override
    public Single<ResultSet> getTimeFrameOfDependablesGroupedByTitle(long from, long to) {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT %s, SUM(%s) FROM %s LEFT JOIN %s ON %s WHERE %s>=? AND %s<=? AND %s=? GROUP BY %s",
                        Db.WATCHDOG_TABLE + "." + TimeLog.WINDOW_TITLE, // select window title
                        TimeLog.ELAPSED, // and sum elapsed
                        Db.WATCHDOG_TABLE, // from watchdog logs
                        Db.CATEGORIZED_TABLE, // and type table
                        Db.WATCHDOG_TABLE + "." + TimeLog.PROCESS_NAME + "=" + Db.CATEGORIZED_TABLE + "." + Type.PROCESS_NAME, // being process names equal
                        TimeLog.EPOCH, // where epoch >= ?
                        TimeLog.EPOCH, // and epoch <= ?
                        Db.CATEGORIZED_TABLE + "." + Type.TYPE, // and type = ?
                        Db.WATCHDOG_TABLE + "." + TimeLog.WINDOW_TITLE)); // group by window title
                stm.setLong(1, from);
                stm.setLong(2, to);
                stm.setString(3, Type.Types.DEPENDS.toString());
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }
    
}
