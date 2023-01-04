package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.*;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleRepository implements TitleDao {
    
    private static TitleRepository instance;
    
    private final Db dbInstance = TitleFactory.getDb();
    
    private TitleRepository() {
    }
    
    public static TitleRepository getInstance() {
        if (instance == null) {
            instance = new TitleRepository();
        }
        return instance;
    }
    
    @Override
    public Single<String> add(Title element) {
        return Db.singleString(() -> tryAddingElement(element));
    }
    
    private String tryAddingElement(Title element) {
        String result = "";
        try {
            result = runDbAddStatement(element);
        } catch (SQLException ex) {
            log.error("Error adding element", ex);
        }
        return result;
    }
    
    private String runDbAddStatement(Title element) throws SQLException {
        PreparedStatement statement = buildDbAddStatement(element);
        statement.executeUpdate();
        return element.getSubStr();
    }
    
    private PreparedStatement buildDbAddStatement(Title element) throws SQLException {
        PreparedStatement statement = dbInstance.prepareStatementWithGeneratedKeys(String.format("INSERT INTO %s (%s, %s) ", 
                Db.TITLES_TABLE, Title.SUB_STR, Title.TYPE)
                + "VALUES (?,?)");
        statement.setString(1, element.getSubStr());
        statement.setString(2, element.getTypeString());
        return statement;
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
                log.error("Error updating element", ex);
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
                log.error("Error finding all", ex);
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
                stm = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
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
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
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
