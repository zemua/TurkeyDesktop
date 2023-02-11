package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionRepository implements ConditionDao {
    
    private ConditionFactory factory;
    private Db dbInstance;
    
    private static ConditionRepository instance;
    
    private ConditionRepository(ConditionFactory conditionFactory) {
        this.factory = conditionFactory;
        this.dbInstance = conditionFactory.getDb();
    }
    
    public static ConditionRepository getInstance(ConditionFactory conditionFactory) {
        if (instance == null) {
            instance = new ConditionRepository(conditionFactory);
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(Condition condition) {
        return dbInstance.singleLong(() -> retrieveAddResultingId(condition));
    }
    
    private long retrieveAddResultingId(Condition condition) {
        long result = -1;
        try {
            result = executeAddReturningId(condition);
        } catch (SQLException ex) {
            log.error("Error adding condition", ex);
        }
        return result;
    }
    
    private long executeAddReturningId(Condition condition) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(condition);
        preparedStatement.executeUpdate();
        return retrieveGeneratedId(preparedStatement);
    }
    
    private PreparedStatement buildAddQuery(Condition condition) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = dbInstance.prepareStatementWithGeneratedKeys(String.format("INSERT INTO %s (%s, %s, %s, %s) ",
                Db.CONDITIONS_TABLE, Condition.GROUP_ID, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION)
                + "VALUES (?, ?, ?, ?)");
        preparedStatement.setLong(1, condition.getGroupId());
        preparedStatement.setLong(2, condition.getTargetId());
        preparedStatement.setLong(3, condition.getUsageTimeCondition());
        preparedStatement.setLong(4, condition.getLastDaysCondition());
        return preparedStatement;
    }
    
    private long retrieveGeneratedId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedId = preparedStatement.getGeneratedKeys();
        if (generatedId.next()) {
            return generatedId.getLong(Condition.ID_POSITION);
        } else {
            throw new SQLException("Couldn't get a generated id");
        }
    }

    @Override
    public Single<Long> update(Condition element) {
        return dbInstance.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.CONDITIONS_TABLE, Condition.TARGET_ID, Condition.USAGE_TIME_CONDITION, Condition.LAST_DAYS_CONDITION, Condition.ID));
                stm.setLong(1, element.getTargetId());
                stm.setLong(2, element.getUsageTimeCondition());
                stm.setLong(3, element.getLastDaysCondition());
                stm.setLong(4, element.getId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating condition", ex);
            }
            return result;
        });
    }

    @Override
    public Single<ResultSet> findAll() {
        return dbInstance.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.CONDITIONS_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all conditions", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(Long id) {
        return dbInstance.singleResultSet(() -> retrieveFindByIdResultSet(id));
    }
    
    private ResultSet retrieveFindByIdResultSet(Long id) {
        ResultSet resultSet = null;
        try {
            resultSet = executeFindById(id);
        } catch (SQLException ex) {
            log.error("Error finding condition by id", ex);
        }
        return resultSet;
    }
    
    private ResultSet executeFindById(Long id) throws SQLException {
        PreparedStatement preparedStatement = buildFindByIdQuery(id);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(Long id) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.CONDITIONS_TABLE, Condition.ID));
        preparedStatement.setLong(1, id);
        return preparedStatement;
    }
    
    @Override
    public Single<ResultSet> findByGroupId(long groupId) {
        return dbInstance.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.GROUP_ID));
                stm.setLong(1, groupId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding condition by group id", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> deleteById(Long id) {
        return dbInstance.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting condition by id", ex);
            }
            return delQty;
        });
    }
    
    @Override
    public Single<Long> deleteByGroupId(long id) {
        return dbInstance.singleLong(() -> {
            long delQty = -1;
                PreparedStatement stm;
                try {
                    stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                            Db.CONDITIONS_TABLE, Condition.GROUP_ID));
                    stm.setLong(1, id);
                    delQty = stm.executeUpdate();
                } catch (SQLException ex) {
                    log.error("Error deleting condition by group id", ex);
                }
            return delQty;
        });
    }
    
    @Override
    public Single<Long> deleteByTargetId(long id) {
        return dbInstance.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.CONDITIONS_TABLE, Condition.TARGET_ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting condition by target id", ex);
            }
            return delQty;
        });
    }
    
}
