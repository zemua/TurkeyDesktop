package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeRepository implements TypeDao {
    
    private Db dbInstance = TypeFactory.getDb();
    
    private static TypeRepository instance;
    
    private TypeRepository(){}
    
    public static TypeRepository getInstance() {
        if (instance == null) {
            instance = new TypeRepository();
        }
        return instance;
    }
    
    @Override
    public Single<String> add(Type type) {
        return Db.singleString(() -> retrieveAddResult(type));
    }
    
    private String retrieveAddResult(Type type) {
        String result = "";
        try {
            result = executeAdd(type);
        } catch (SQLException ex) {
            log.error("Error adding Type " + type, ex);
        }
        return result;
    }
    
    private String executeAdd(Type type) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(type);
        preparedStatement.executeUpdate();
        return type.getProcess();
    }
    
    private PreparedStatement buildAddQuery(Type type) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatement(String.format("INSERT INTO %s (%s, %s) ",
                Db.CATEGORIZED_TABLE, Type.PROCESS_NAME, Type.TYPE)
                + "VALUES (?,?)");
        preparedStatement.setString(1, type.getProcess());
        preparedStatement.setString(2, type.getType().toString());
        return preparedStatement;
    }

    @Override
    public Single<Long> update(Type type) {
        return Db.singleLong(() -> {
            long entriesUpdated = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=? WHERE %s=?",
                        Db.CATEGORIZED_TABLE, Type.TYPE, Type.PROCESS_NAME));
                stm.setString(1, type.getType().toString());
                stm.setString(2, type.getProcess());
                entriesUpdated = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating Type " + type, ex);
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
                log.error("Error finding all Types", ex);
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
                log.error("Error finding Type by type: " + type, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(String id) {
        return Db.singleResultSet(() -> retrieveFindById(id));
    }
    
    private ResultSet retrieveFindById(String id) {
        ResultSet result = null;
        try {
            result = executeFind(id);
        } catch (SQLException ex) {
            log.error("Error finding Type by id: " + id, ex);
        }
        return result;
    }
    
    private ResultSet executeFind(String id) throws SQLException {
        PreparedStatement preparedStatement = buildFindByIdQuery(id);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(String id) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.CATEGORIZED_TABLE, Type.PROCESS_NAME));
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
                        Db.CATEGORIZED_TABLE, Type.PROCESS_NAME));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting Type by id: " + id, ex);
            }
            return delQty;
        });
    }
    
}
