package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupAssignationRepository implements GroupAssignationDao {
    
    private final GroupAssignationFactory factory;
    private final Db db;
    
    public GroupAssignationRepository(GroupAssignationFactory groupAssignationFactory) {
        this.factory = groupAssignationFactory;
        this.db = factory.getDb();
    }
    
    @Override
    public Single<ResultSet> findByElementId(GroupAssignation.ElementType elementType, String elementId) {
        return db.singleResultSet(() -> retrieveFindResult(elementType, elementId));
    }
    
    private ResultSet retrieveFindResult(GroupAssignation.ElementType elementType, String elementId) {
        ResultSet resultSet = null;
        try {
            resultSet = executeFindElement(elementType, elementId);
        } catch (SQLException ex) {
            log.error("Error finding Group Assignation by id " + elementId + " and type " + elementType, ex);
        }
        return resultSet;
    }
    
    private ResultSet executeFindElement(GroupAssignation.ElementType elementType, String elementId) throws SQLException {
        PreparedStatement preparedStatement = buildFindQuery(elementType, elementId);
        return preparedStatement.executeQuery();
    }
    
    private PreparedStatement buildFindQuery(GroupAssignation.ElementType elementType, String elementId) throws SQLException {
        PreparedStatement preparedStatement = db.prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID));
        preparedStatement.setString(1, elementType.toString());
        preparedStatement.setString(2, elementId);
        return preparedStatement;
    }

    @Override
    public Single<ResultSet> findAllElementTypeOfGroup(GroupAssignation.ElementType elementType, Long groupId) {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.GROUP_ID));
                stm.setString(1, elementType.toString());
                stm.setLong(2, groupId);
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all elements of type " + elementType + " for group " + groupId, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<ElementId> add(GroupAssignation groupAssignation) {
        return db.<ElementId>singleGeneric(() -> retrieveAddResult(groupAssignation));
    }
    
    private ElementId retrieveAddResult(GroupAssignation groupAssignation) {
        ElementId elementIdResult = new ElementId(groupAssignation.getType(), "");
        try {
            elementIdResult = executeAdd(groupAssignation);
        } catch (SQLException ex) {
            log.error("Error adding group assignation " + groupAssignation, ex);
        }
        return elementIdResult;
    }
    
    private ElementId executeAdd(GroupAssignation groupAssignation) throws SQLException {
        PreparedStatement preparedStatement = buildAddQuery(groupAssignation);
        preparedStatement.executeUpdate();
        return new ElementId(groupAssignation.getType(), groupAssignation.getElementId());
    }
    
    private PreparedStatement buildAddQuery(GroupAssignation groupAssignation) throws SQLException {
        PreparedStatement preparedStatement = db.prepareStatement(String.format("INSERT INTO %s (%s, %s, %s) ",
                Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID, GroupAssignation.GROUP_ID)
                + "VALUES (?, ?, ?)");
        preparedStatement.setString(1, groupAssignation.getType().toString());
        preparedStatement.setString(2, groupAssignation.getElementId());
        preparedStatement.setLong(3, groupAssignation.getGroupId());
        return preparedStatement;
    }

    @Override
    public Single<Long> update(GroupAssignation element) {
        return db.singleLong(() -> {
            long result = -1;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? ",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID, GroupAssignation.GROUP_ID));
                stm.setString(1, element.getType().toString());
                stm.setString(2, element.getElementId());
                stm.setLong(3, element.getGroupId());
                result = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error updating group assignation " + element, ex);
            }
            return result;
        });
        
    }

    @Override
    public Single<ResultSet> findAll() {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT * FROM %s",
                        Db.GROUP_ASSIGNATION_TABLE));
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all group assignations", ex);
            }
            return rs;
        });
    }

    @Deprecated
    @Override
    public Single<ResultSet> findById(GroupAssignationDao.ElementId id) {
        throw new RuntimeException("Not supported operation");
    }

    @Deprecated
    @Override
    public Single<Long> deleteById(GroupAssignationDao.ElementId id) {
        throw new RuntimeException("Not supported operation");
    }
    
    @Override
    public Single<Long> deleteByElementId(GroupAssignation.ElementType elementType, String elementId) {
        return db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=? AND %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE, GroupAssignation.ELEMENT_ID));
                stm.setString(1, elementType.toString());
                stm.setString(2, elementId);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting assignation by id " + elementId + " of type " + elementType, ex);
            }
            return delQty;
        });
        
    }
    
    @Override
    public Single<ResultSet> findAllOfType(GroupAssignation.ElementType elementType) {
        return db.singleResultSet(() -> {
            ResultSet rs = null;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.TYPE));
                stm.setString(1, elementType.toString());
                rs = stm.executeQuery();
            } catch (SQLException ex) {
                log.error("Error finding all assignations of type " + elementType, ex);
            }
            return rs;
        });
    }

    @Override
    public Single<Long> deleteByGroupId(long groupId) {
        return db.singleLong(() -> {
            long delQty = -1;
            PreparedStatement stm;
            try {
                stm = db.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE %s=?",
                        Db.GROUP_ASSIGNATION_TABLE, GroupAssignation.GROUP_ID));
                stm.setLong(1, groupId);
                delQty = stm.executeUpdate();
            } catch (SQLException ex) {
                log.error("Error deleting assignations by group id " + groupId, ex);
            }
            return delQty;
        });
    }
    
}
