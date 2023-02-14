package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;
import java.sql.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleRepository implements TitleDao {
    
    private final Db db;
    
    public TitleRepository(TitleFactory titleFactory) {
        this.db = titleFactory.getDb();
    }
    
    @Override
    public Single<String> add(Title element) {
        return db.singleString(() -> retrieveAddResult(element));
    }
    
    private String retrieveAddResult(Title element) {
        String result = "";
        try {
            result = executeAdd(element);
        } catch (SQLException ex) {
            log.error("Error adding element", ex);
        }
        return result;
    }
    
    private String executeAdd(Title element) throws SQLException {
        PreparedStatement statement = buildDbAddStatement(element);
        statement.executeUpdate();
        return element.getSubStr();
    }
    
    private PreparedStatement buildDbAddStatement(Title element) throws SQLException {
        PreparedStatement statement = db.prepareStatementWithGeneratedKeys(String.format("INSERT INTO %s (%s, %s) ", 
                Db.TITLES_TABLE, Title.SUB_STR, Title.TYPE)
                + "VALUES (?,?)");
        statement.setString(1, element.getSubStr());
        statement.setString(2, element.getTypeString());
        return statement;
    }

    @Override
    public Single<Long> update(Title element) {
        return db.singleLong(() -> {
            long entriesUpdated = -1;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("UPDATE %s SET %s=? WHERE %s=?",
                        Db.TITLES_TABLE, Title.TYPE, Title.SUB_STR));
                stm.setString(1, element.getType().toString());
                stm.setString(2, element.getSubStr());
                entriesUpdated = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating element", ex);
            }
            return entriesUpdated;
        });
    }

    @Override
    public Single<ResultSet> findAll() {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.TITLES_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(String id) {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.TITLES_TABLE, Title.SUB_STR));
                stm.setString(1, id);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error in find by id", ex);
            }
            return rs;
        });
        
    }

    @Override
    public Single<Long> deleteById(String id) {
        return db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.TITLES_TABLE, Title.SUB_STR));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting by id", ex);
            }
            return delQty;
        });
    }
    
}
