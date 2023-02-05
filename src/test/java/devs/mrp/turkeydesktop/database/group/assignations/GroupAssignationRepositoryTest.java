package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao.ElementId;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupAssignationRepositoryTest {
    
    static Db db = CommonMocks.getMock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static GroupAssignationRepository groupAssignationRepository;
    
    GroupAssignation groupAssignation;
    
    @BeforeClass
    public static void classSetup() {
        DbFactoryImpl.setDbSupplier(() -> db);
        groupAssignationRepository = GroupAssignationRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        groupAssignation = new GroupAssignation();
        groupAssignation.setElementId("some process or title");
        groupAssignation.setGroupId(1);
        groupAssignation.setType(GroupAssignation.ElementType.TITLE);
    }

    @Test
    public void testFindByElementIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet groupResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUP_ASSIGNATION_TABLE WHERE TYPE=? AND ELEMENT_ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(groupResult);
        
        ResultSet result = groupAssignationRepository.findByElementId(groupAssignation.getType(), groupAssignation.getElementId()).blockingGet();
        assertEquals(groupResult, result);
        verify(preparedStatement, times(1)).setString(1, groupAssignation.getType().toString());
        verify(preparedStatement, times(1)).setString(2, groupAssignation.getElementId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ElementId expectedResult = new ElementId(groupAssignation.getType(), groupAssignation.getElementId());
        
        when(db.prepareStatement("INSERT INTO GROUP_ASSIGNATION_TABLE (TYPE, ELEMENT_ID, GROUP_ID) VALUES (?, ?, ?)"))
                .thenReturn(preparedStatement);
        
        ElementId idResult = groupAssignationRepository.add(groupAssignation).blockingGet();
        assertEquals(expectedResult, idResult);
        verify(preparedStatement, times(1)).setString(1, groupAssignation.getType().toString());
        verify(preparedStatement, times(1)).setString(2, groupAssignation.getElementId());
        verify(preparedStatement, times(1)).setLong(3, groupAssignation.getGroupId());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ElementId expectedResult = new ElementId(groupAssignation.getType(), "");
        
        when(db.prepareStatement("INSERT INTO GROUP_ASSIGNATION_TABLE (TYPE, ELEMENT_ID, GROUP_ID) VALUES (?, ?, ?)"))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        ElementId idResult = groupAssignationRepository.add(groupAssignation).blockingGet();
        assertEquals(expectedResult, idResult);
        verify(preparedStatement, times(1)).setString(1, groupAssignation.getType().toString());
        verify(preparedStatement, times(1)).setString(2, groupAssignation.getElementId());
        verify(preparedStatement, times(1)).setLong(3, groupAssignation.getGroupId());
    }
    
}
