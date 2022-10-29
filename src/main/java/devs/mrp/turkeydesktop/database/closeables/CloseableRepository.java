/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class CloseableRepository implements CloseableDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(CloseableRepository.class.getName());
    private Semaphore semaphore = Db.getSemaphore();
    
    private static CloseableRepository instance;
    
    private CloseableRepository() {}
    
    public static CloseableRepository getInstance() {
        if (instance == null) {
            instance = new CloseableRepository();
        }
        return instance;
    }
    
    @Override
    public Observable<Long> add(Closeable element) {
        return Db.observableLong(()-> {
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s) ",
                        Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME)
                        + "VALUES (?)");
                        // we don't retrieve generated keys as no keys are generated, we provide them
                stm.setString(1, element.getProcess());
                stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
                return 0L;
            }
            return 1L;
        });
        
    }

    @Deprecated
    @Override
    public Observable<Long> update(Closeable element) {
        // single column table, nothing to update
        return Observable.just(0L);
    }

    @Override
    public Observable<ResultSet> findAll() {
        return Db.observableResultSet(() -> {
            ResultSet rs = null;
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CLOSEABLES_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }

    @Override
    public Observable<ResultSet> findById(String id) {
        return Db.observableResultSet(() -> {
            ResultSet rs = null;
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME));
                stm.setString(1, id);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return rs;
        });
    }

    @Override
    public Observable<Long> deleteById(String id) {
        return Db.observableLong(() -> {
            long delQty = -1;
            semaphore.acquire();
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
}
