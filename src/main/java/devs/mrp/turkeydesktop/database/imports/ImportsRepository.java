package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportsRepository implements ImportsDao {
    
    private Db db;
    
    public ImportsRepository(ImportFactory importFactory) {
        this.db = importFactory.getDb();
    }
    
    @Override
    public Single<String> add(String importPath) {
        return db.singleString(() -> retrieveAddResult(importPath));
    }
    
    private String retrieveAddResult(String importPath) {
        String result = "";
        try {
            result = executeAdd(importPath);
        } catch (SQLException ex) {
            log.error("Error adding Imports", ex);
        }
        return result;
    }
    
    private String executeAdd(String importPath) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(importPath);
        preparedStatement.executeUpdate();
        return importPath;
    }
    
    private PreparedStatement buildAddQuery(String importPath) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = db.prepareStatement(String.format("INSERT INTO %s (%s) ",
                Db.IMPORTS_TABLE, ConfigurationEnum.IMPORT_PATH.toString())
                + "VALUES (?)");
        preparedStatement.setString(1, importPath);
        return preparedStatement;
    }

    @Deprecated
    @Override
    public Single<Long> update(String element) {
        throw new RuntimeException("operation not supported");
    }

    @Override
    public Single<ResultSet> findAll() {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT * FROM %s",
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
        return db.singleResultSet(() -> retrieveFindByIdResult(id));
    }
    
    private ResultSet retrieveFindByIdResult(String id) {
        ResultSet resultSet = null;
        try {
            resultSet = executeFindById(id);
        } catch (SQLException ex) {
            log.error("Error finding Import by id", ex);
        }
        return resultSet;
    }
    
    private ResultSet executeFindById(String id) throws SQLException {
        return buildFindByIdQuery(id)
                .executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(String id) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = db.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.IMPORTS_TABLE, ConfigurationEnum.IMPORT_PATH.toString()));
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    @Override
    public Single<Long> deleteById(String id) {
        return db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
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
