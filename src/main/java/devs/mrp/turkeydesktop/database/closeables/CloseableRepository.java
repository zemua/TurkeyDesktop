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
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class CloseableRepository implements CloseableDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(CloseableRepository.class.getName());
    
    private static CloseableRepository instance;
    
    private CloseableRepository() {}
    
    static CloseableRepository getInstance() {
        if (instance == null) {
            instance = new CloseableRepository();
        }
        return instance;
    }
    
    @Override
    public Single<String> add(Closeable element) {
        return Db.singleString(()-> {
            PreparedStatement stm;
            String result = "";
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s) ",
                        Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME)
                        + "VALUES (?)");
                        // we don't retrieve generated keys as no keys are generated, we provide them
                stm.setString(1, element.getProcess());
                stm.executeUpdate();
                result = element.getProcess();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
        
    }

    @Deprecated
    @Override
    public Single<Long> update(Closeable element) {
        // single column table, nothing to update
        return Single.just(0L);
    }

    @Override
    public Single<ResultSet> findAll() {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
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
    public Single<ResultSet> findById(String id) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
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
    public Single<Long> deleteById(String id) {
        return Db.singleLong(() -> {
            long delQty = -1;
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
