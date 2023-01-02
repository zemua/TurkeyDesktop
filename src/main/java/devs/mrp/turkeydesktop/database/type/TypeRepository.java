/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

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
public class TypeRepository implements TypeDao {
    
    private Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(TypeRepository.class.getName());
    
    private static TypeRepository instance;
    
    private TypeRepository(){}
    
    public static TypeRepository getInstance() {
        if (instance == null) {
            instance = new TypeRepository();
        }
        return instance;
    }
    
    @Override
    public Single<String> add(Type element) {
        return Db.singleString(() -> {
            String result = "";
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s) ",
                        Db.CATEGORIZED_TABLE, Type.PROCESS_NAME, Type.TYPE)
                        + "VALUES (?,?)");
                        // we don't retrieve generated keys as no keys are generated, we provide them
                stm.setString(1, element.getProcess());
                stm.setString(2, element.getType().toString());
                stm.executeUpdate();
                result = element.getProcess();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Single<Long> update(Type element) {
        return Db.singleLong(() -> {
            long entriesUpdated = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=? WHERE %s=?",
                        Db.CATEGORIZED_TABLE, Type.TYPE, Type.PROCESS_NAME));
                stm.setString(1, element.getType().toString());
                stm.setString(2, element.getProcess());
                entriesUpdated = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return entriesUpdated;
        });
    }

    @Override
    public Single<ResultSet> findAll() {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CATEGORIZED_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null ,ex);
            }
            return rs;
        });
    }
    
    @Override
    public Single<ResultSet> findByType(String type) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CATEGORIZED_TABLE, Type.TYPE));
                stm.setString(1, type);
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
                        Db.CATEGORIZED_TABLE, Type.PROCESS_NAME));
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
                        Db.CATEGORIZED_TABLE, Type.PROCESS_NAME));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
}
