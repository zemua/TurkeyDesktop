package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloseableRepository implements CloseableDao {
    
    private CloseableFactory factory;
    private Db db;
    
    public CloseableRepository(CloseableFactory factory) {
        this.factory = factory;
        db = factory.getDb();
    }
    
    @Override
    public Single<String> add(Closeable element) {
        return db.singleString(()-> retrieveAddResult(element));
    }
    
    public String retrieveAddResult(Closeable element) {
        String result = "";
        try {
            result = executeAdd(element);
        } catch (SQLException ex) {
            log.error("Error adding closeable", ex);
        }
        return result;
    }
    
    private String executeAdd(Closeable element) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(element);
        preparedStatement.executeUpdate();
        return element.getProcess();
    }
    
    private PreparedStatement buildAddQuery(Closeable element) throws SQLException {
        PreparedStatement preparedStatement = db.prepareStatement(String.format("INSERT INTO %s (%s) ",
                Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME)
                + "VALUES (?)");
        preparedStatement.setString(1, element.getProcess());
        return preparedStatement;
    }

    @Deprecated
    @Override
    public Single<Long> update(Closeable element) {
        throw new RuntimeException("Unsupported operation");
    }

    @Override
    public Single<ResultSet> findAll() {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CLOSEABLES_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all closeables", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(String id) {
        return db.singleResultSet(() -> retrieveFindByIdResult(id));
    }
    
    private ResultSet retrieveFindByIdResult(String id) {
        ResultSet result = null;
        try {
            result = executeFindById(id);
        } catch (SQLException ex) {
            log.error("Error finding by Id", ex);
        }
        return result;
    }
    
    private ResultSet executeFindById(String id) throws SQLException {
        PreparedStatement preparedStatement = buildFindByIdQuery(id);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(String id) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = db.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME));
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
                        Db.CLOSEABLES_TABLE, Closeable.PROCESS_NAME));
                stm.setString(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting by id", ex);
            }
            return delQty;
        });
    }
    
}
