package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportedGroupRepository implements ExportedGroupDao {
    
    private final Db dbInstance = DbFactory.getDb();
    
    private static ExportedGroupRepository instance;
    
    private ExportedGroupRepository() {
        
    }
    
    public static ExportedGroupRepository getInstance() {
        if (instance == null) {
            instance = new ExportedGroupRepository();
        }
        return instance;
    }
    
    @Override
    public Single<ResultSet> findByGroup(Long id) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP));
                stm.setLong(1, id);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding exported group by id " + id, ex);
            }
            return rs;
        });
        
    }

    @Override
    public Single<ResultSet> findByGroupAndFile(Long groupId, String file) {
        return Db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE));
                stm.setLong(1, groupId);
                stm.setString(2, file);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding exported group by id " + groupId + " and file " + file, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> deleteByGroup(Long id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP));
                stm.setLong(1, id);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting by group id " + id, ex);
            }
            return delQty;
        });
    }

    @Override
    public Single<ExportedGroupId> add(ExportedGroup exportedGroup) {
        return Db.<ExportedGroupId>singleGeneric(() -> retrieveIdFromAddedExport(exportedGroup));
    }
    
    private ExportedGroupId retrieveIdFromAddedExport(ExportedGroup exportedGroup) {
        ExportedGroupId resultId = new ExportedGroupId(-1, "");
        try {
            resultId = executeAdd(exportedGroup);
        } catch (SQLException ex) {
            log.error("Error adding exported group " + exportedGroup, ex);
        }
        return resultId;
    }
    
    private ExportedGroupId executeAdd(ExportedGroup exportedGroup) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(exportedGroup);
        preparedStatement.executeUpdate();
        return new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile());
    }
    
    private PreparedStatement buildAddQuery(ExportedGroup exportedGroup) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatement(String.format("INSERT INTO %s (%s, %s, %s) ",
                Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE, ExportedGroup.DAYS)
                + "VALUES (?, ?, ?)");
        preparedStatement.setLong(1, exportedGroup.getGroup());
        preparedStatement.setString(2, exportedGroup.getFile());
        preparedStatement.setLong(3, exportedGroup.getDays());
        return preparedStatement;
    }

    @Override
    public Single<Long> update(ExportedGroup exportedGroup) {
        return Db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=? WHERE %s=? ",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.FILE, ExportedGroup.DAYS, ExportedGroup.GROUP));
                stm.setString(1, exportedGroup.getFile());
                stm.setLong(2, exportedGroup.getDays());
                stm.setLong(3, exportedGroup.getGroup());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating exported group " + exportedGroup, ex);
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
                        Db.GROUPS_EXPORT_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all exported groups", ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ResultSet> findById(ExportedGroupId id) {
        return Db.singleResultSet(() -> retrieveExportedGroupById(id));
    }
    
    private ResultSet retrieveExportedGroupById(ExportedGroupId id) {
        ResultSet resultSet = null;
        try {
            resultSet = executeFind(id);
        } catch (SQLException ex) {
            log.error("Error finding exported group by id " + id, ex);
        }
        return resultSet;
    }
    
    private ResultSet executeFind(ExportedGroupId id) throws SQLException {
        PreparedStatement preparedStatement = buildFindQuery(id);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindQuery(ExportedGroupId id) throws SQLException {
        PreparedStatement preparedStatement = dbInstance.prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE));
        preparedStatement.setLong(1, id.getGroup());
        preparedStatement.setString(2, id.getFile());
        return preparedStatement;
    }

    @Override
    public Single<Long> deleteById(ExportedGroupId id) {
        return Db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = dbInstance.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=? AND %s=?",
                        Db.GROUPS_EXPORT_TABLE, ExportedGroup.GROUP, ExportedGroup.FILE));
                stm.setLong(1, id.getGroup());
                stm.setString(2, id.getFile());
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting exported group by id " + id, ex);
            }
            return delQty;
        });
    }
    
}
