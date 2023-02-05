package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalGroupRepository implements ExternalGroupDao {
    
    private Db dbInstance = DbFactoryImpl.getDb();
    
    private static ExternalGroupRepository instance;
    
    private ExternalGroupRepository() {
    }
    
    public static ExternalGroupRepository getInstance() {
        if (instance == null) {
            instance = new ExternalGroupRepository();
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(ExternalGroup externalGroup) {
        return Db.singleLong(() -> retrieveAddGeneratedId(externalGroup));
    }
    
    private long retrieveAddGeneratedId(ExternalGroup externalGroup) {
        long idResult = -1;
        try {
            idResult = executeAdd(externalGroup);
        } catch (SQLException ex) {
            log.error("Error adding ExternalGroup " + externalGroup, ex);
        }
        return idResult;
    }
    
    private long executeAdd(ExternalGroup externalGroup) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(externalGroup);
        preparedStatement.executeUpdate();
        return fetchGeneratedId(preparedStatement);
    }
    
    private PreparedStatement buildAddQuery(ExternalGroup externalGroup) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatementWithGeneratedKeys(String.format("INSERT INTO %s (%s, %s) ",
                Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.GROUP, ExternalGroup.FILE)
                + "VALUES (?, ?)");
        preparedStatement.setLong(1, externalGroup.getGroup());
        preparedStatement.setString(2, externalGroup.getFile());
        return preparedStatement;
    }
    
    private long fetchGeneratedId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedId = preparedStatement.getGeneratedKeys();
        if (generatedId.next()) {
            return generatedId.getLong(ExternalGroup.ID_COLUMN);
        } else {
            throw new SQLException("Not able to retrieve generated id");
        }
    }

    @Override
    public Single<Long> update(ExternalGroup externalGroup) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=? WHERE %s=? ",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.GROUP, ExternalGroup.FILE, ExternalGroup.ID));
                stm.setLong(1, externalGroup.getGroup());
                stm.setString(2, externalGroup.getFile());
                stm.setLong(3, externalGroup.getId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating external group " + externalGroup, ex);
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
                        Db.GROUPS_EXTERNAL_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all external groups", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(Long id) {
        return Db.singleResultSet(() -> retrieveById(id));
    }
    
    private ResultSet retrieveById(Long id) {
        ResultSet resultSet = null;
        try {
            resultSet = executeFindById(id);
        } catch (SQLException ex) {
            log.error("Error finding external group by id " + id, ex);
        }
        return resultSet;
    }
    
    private ResultSet executeFindById(Long id) throws SQLException {
        PreparedStatement preparedStatement = buildFindByIdQuery(id);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(Long id) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.ID));
        preparedStatement.setLong(1, id);
        return preparedStatement;
    }

    @Override
    public Single<Long> deleteById(Long id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting external group by id " + id, ex);
            }
            return delQty;
        });
    }

    @Override
    public Single<ResultSet> findByGroup(Long groupId) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.GROUP));
                stm.setLong(1, groupId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding external group by group id " + groupId, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findByFile(String file) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.FILE));
                stm.setString(1, file);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding external group by file " + file, ex);
            }
            return rs;
        });
        
    }

    @Override
    public Single<Long> deleteByGroup(Long groupId) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.GROUP));
                stm.setLong(1, groupId);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting external groups by group " + groupId, ex);
            }
            return delQty;
        });
        
    }

    @Override
    public Single<ResultSet> findByGroupAndFile(Long groupId, String file) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUPS_EXTERNAL_TABLE, ExternalGroup.GROUP, ExternalGroup.FILE));
                stm.setLong(1, groupId);
                stm.setString(2, file);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding external group by group id " + groupId + " and file " + file, ex);
            }
            return rs;
        });
    }
    
}
