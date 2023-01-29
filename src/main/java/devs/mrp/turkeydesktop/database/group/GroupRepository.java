package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupRepository implements GroupDao {
    
    private Db dbInstance = DbFactory.getDb();
    
    private static GroupRepository instance;
    
    private GroupRepository() {
    }
    
    public static GroupRepository getInstance() {
        if (instance == null) {
            instance = new GroupRepository();
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(Group group) {
        return Db.singleLong(() -> retrieveAddGeneratedId(group));
    }
    
    private long retrieveAddGeneratedId(Group group) {
        long longGeneratedId = -1;
        try {
            longGeneratedId = executeAdd(group);
        } catch (SQLException ex) {
            log.error("Error adding Group " + group, ex);
        }
        return longGeneratedId;
    }
    
    private long executeAdd(Group group) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(group);
        preparedStatement.executeUpdate();
        return retrieveGeneratedId(preparedStatement);
    }
    
    private PreparedStatement buildAddQuery(Group group) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatementWithGeneratedKeys(String.format("INSERT INTO %s (%s, %s) ",
                Db.GROUPS_TABLE, Group.NAME, Group.TYPE)
                + "VALUES (?, ?)");
        preparedStatement.setString(1, group.getName());
        preparedStatement.setString(2, group.getType().toString());
        return preparedStatement;
    }
    
    private long retrieveGeneratedId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedId = preparedStatement.getGeneratedKeys();
        if (generatedId.next()) {
            return generatedId.getLong(Group.ID_COLUMN);
        } else {
            throw new SQLException("Could not retrieve generated id");
        }
    }

    @Override
    public Single<Long> update(Group element) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.GROUPS_TABLE, Group.NAME, Group.TYPE, Group.PREVENT_CLOSE, Group.ID));
                stm.setString(1, element.getName());
                stm.setString(2, element.getType().toString());
                stm.setBoolean(3, element.isPreventClose());
                stm.setLong(4, element.getId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating Group " + element, ex);
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
                            Db.GROUPS_TABLE));
                    rs = stm.executeQuery();
                } catch (SQLException ex) {
                    log.error("Error finding all Groups", ex);
                }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(Long id) {
        return Db.singleResultSet(() -> {
            ResultSet resultSet = null;
            try {
                resultSet = retrieveById(id);
            } catch (SQLException ex) {
                log.error("Error finding Group by id " + id, ex);
            }
            return resultSet;
        });
    }
    
    private ResultSet retrieveById(Long id) throws SQLException {
        PreparedStatement preparedStatement = buildFindByIdQuery(id);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindByIdQuery(Long id) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                Db.GROUPS_TABLE, Group.ID));
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
                        Db.GROUPS_TABLE, Group.ID));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting Group by id " + id, ex);
            }
            return delQty;
        });
    }

    @Override
    public Single<ResultSet> findAllOfType(Group.GroupType type) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUPS_TABLE, Group.TYPE));
                stm.setString(1, type.toString());
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all groups of type " + type, ex);
            }
            return rs;
        });
    }
    
    @Override
    public Single<Integer> setPreventClose(long groupId, boolean preventClose) {
        return Db.singleInt(() -> {
            int affectedRows = 0;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                        Db.GROUPS_TABLE, Group.PREVENT_CLOSE, Group.ID));
                stm.setBoolean(1, preventClose);
                stm.setLong(2, groupId);
                affectedRows = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error setting prevent close " + preventClose +  " for groupId " + groupId, ex);
            }
            return affectedRows;
        });
    }
    
}
