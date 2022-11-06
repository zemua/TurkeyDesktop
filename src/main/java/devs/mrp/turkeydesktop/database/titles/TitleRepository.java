/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Single;

/**
 *
 * @author miguel
 */
public class TitleRepository implements TitleDao {
    
    private final Db dbInstance = Db.getInstance();
    private Logger logger = Logger.getLogger(TitleRepository.class.getName());
    
    private static TitleRepository instance;
    
    private TitleRepository() {
        
    }
    
    public static TitleRepository getInstance() {
        if (instance == null) {
            instance = new TitleRepository();
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(Title element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("INSERT INTO %s (%s, %s) ", 
                        Db.TITLES_TABLE, Title.SUB_STR, Title.TYPE)
                        + "VALUES (?,?)");
                stm.setString(1, element.getSubStr());
                stm.setString(2, element.getType().toString());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return result;
        });
    }

    @Override
    public Single<Long> update(Title element) {
        return Db.singleLong(() -> {
            long entriesUpdated = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=? WHERE %s=?",
                        Db.TITLES_TABLE, Title.TYPE, Title.SUB_STR));
                stm.setString(1, element.getType().toString());
                stm.setString(2, element.getSubStr());
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
                        Db.TITLES_TABLE));
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
                        Db.TITLES_TABLE, Title.SUB_STR));
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
                        Db.TITLES_TABLE, Title.SUB_STR));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return delQty;
        });
    }
    
}
