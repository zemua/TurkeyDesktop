package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementRepository implements ConfigElementDao {
    
    private Db dbInstance = DbFactory.getDb();
    
    private static ConfigElementRepository instance;
    
    private ConfigElementRepository() {
        
    }
    
    public static ConfigElementRepository getInstance() {
        if (instance == null) {
            instance = new ConfigElementRepository();
        }
        return instance;
    }
    
    @Override
    public Single<String> add(ConfigElement element) {
        return Db.singleString(() -> {
            String result = "";
            try {
                result = tryAdd(element);
            } catch (SQLException ex) {
                log.error("Error adding config element", ex);
            }
            return result;
        });
    }
    
    private String tryAdd(ConfigElement element) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(element);
        preparedStatement.executeUpdate();
        return element.getKey().toString();
    }
    
    private PreparedStatement buildAddQuery(ConfigElement element) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = dbInstance.prepareStatementWithGeneratedKeys(String.format("INSERT INTO %s (%s, %s) ",
                Db.CONFIG_TABLE, ConfigElement.KEY, ConfigElement.VALUE)
                + "VALUES (?, ?)");
        preparedStatement.setString(1, element.getKey().toString());
        preparedStatement.setString(2, element.getValue());
        return preparedStatement;
    }

    @Override
    public Single<Long> update(ConfigElement element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=? WHERE %s=? ",
                        Db.CONFIG_TABLE, ConfigElement.VALUE, ConfigElement.KEY));
                stm.setString(1, element.getValue());
                stm.setString(2, element.getKey().toString());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating config element", ex);
            }
            return result;
        });
        
    }

    @Override
    public Single<ResultSet> findAll() {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CONFIG_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all config elements", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(String id) {
        return Db.singleResultSet(() -> {
            return doFind(id);
        });
    }
    
    private ResultSet doFind(String id) {
        ResultSet resultSet = null;
        try {
            resultSet = tryFindById(id);
        } catch (SQLException ex) {
            log.error("Error finding by id", ex);
        }
        return resultSet;
    }
    
    private ResultSet tryFindById(String id) throws SQLException {
        PreparedStatement statement = buildFindByIdQuery(id);
        return statement.executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(String id) throws SQLException {
        PreparedStatement statement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.CONFIG_TABLE, ConfigElement.KEY));
        statement.setString(1, id);
        return statement;
    }

    @Override
    public Single<Long> deleteById(String id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONFIG_TABLE, ConfigElement.KEY));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting by id", ex);
            }
            return delQty;
        });
    }
    
}
