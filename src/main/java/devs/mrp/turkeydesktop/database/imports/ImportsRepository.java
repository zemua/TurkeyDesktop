package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportsRepository implements ImportsDao {
    
    private Db dbInstance = DbFactory.getDb();
    
    private static ImportsRepository instance;
    
    private ImportsRepository() {
    }
    
    public static ImportsRepository getInstance() {
        if (instance == null) {
            instance = new ImportsRepository();
        }
        return instance;
    }
    
    @Override
    public Single<String> add(String element) {
        return Db.singleString(() -> {
            String result = "";
            PreparedStatement stm;
            try {
                stm = dbInstance.prepareStatement(String.format("INSERT INTO %s (%s) ",
                        Db.IMPORTS_TABLE, ConfigurationEnum.IMPORT_PATH.toString())
                        + "VALUES (?)");
                stm.setString(1, element);
                stm.executeUpdate();
                result = element;
            } catch (SQLException ex) {
                log.error("Error adding Imports", ex);
            }
            return result;
        });
    }

    @Deprecated
    @Override
    public Single<Long> update(String element) {
        throw new RuntimeException("operation not supported");
    }

    @Override
    public Single<ResultSet> findAll() {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.IMPORTS_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all Imports", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(String id) {
        return Db.singleResultSet(() -> doFindById(id));
    }
    
    private ResultSet doFindById(String id) {
        ResultSet resultSet = null;
        try {
            resultSet = tryFindById(id);
        } catch (SQLException ex) {
            log.error("Error finding Import by id", ex);
        }
        return resultSet;
    }
    
    private ResultSet tryFindById(String id) throws SQLException {
        return buildFindByIdQuery(id)
                .executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(String id) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.IMPORTS_TABLE, ConfigurationEnum.IMPORT_PATH.toString()));
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    @Override
    public Single<Long> deleteById(String id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.IMPORTS_TABLE, ConfigurationEnum.IMPORT_PATH.toString()));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting Import by id", ex);
            }
            return delQty;
        });
    }
    
}
