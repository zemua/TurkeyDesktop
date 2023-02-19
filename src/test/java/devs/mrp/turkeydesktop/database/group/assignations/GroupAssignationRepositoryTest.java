package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao.ElementId;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupAssignationRepositoryTest {
    
    Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    GroupAssignationRepository groupAssignationRepository;
    GroupAssignationFactory factory = mock(GroupAssignationFactory.class);
    
    GroupAssignation groupAssignation;
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        groupAssignationRepository = new GroupAssignationRepository(factory);
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
    public void testFindByElementIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet groupResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUP_ASSIGNATION_TABLE WHERE TYPE=? AND ELEMENT_ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(groupResult);
        
        groupAssignationRepository.findByElementId(groupAssignation.getType(), groupAssignation.getElementId());
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet result = captor.getValue().call();
        
        assertEquals(groupResult, result);
        verify(preparedStatement, times(1)).setString(1, groupAssignation.getType().toString());
        verify(preparedStatement, times(1)).setString(2, groupAssignation.getElementId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ElementId expectedResult = new ElementId(groupAssignation.getType(), groupAssignation.getElementId());
        
        when(db.prepareStatement("INSERT INTO GROUP_ASSIGNATION_TABLE (TYPE, ELEMENT_ID, GROUP_ID) VALUES (?, ?, ?)"))
                .thenReturn(preparedStatement);
        
        groupAssignationRepository.add(groupAssignation);
        ArgumentCaptor<Callable<ElementId>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleGeneric(captor.capture());
        ElementId idResult = captor.getValue().call();
        
        assertEquals(expectedResult, idResult);
        verify(preparedStatement, times(1)).setString(1, groupAssignation.getType().toString());
        verify(preparedStatement, times(1)).setString(2, groupAssignation.getElementId());
        verify(preparedStatement, times(1)).setLong(3, groupAssignation.getGroupId());
    }
    
    @Test
    public void testAddException() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ElementId expectedResult = new ElementId(groupAssignation.getType(), "");
        
        when(db.prepareStatement("INSERT INTO GROUP_ASSIGNATION_TABLE (TYPE, ELEMENT_ID, GROUP_ID) VALUES (?, ?, ?)"))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        groupAssignationRepository.add(groupAssignation);
        ArgumentCaptor<Callable<ElementId>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleGeneric(captor.capture());
        ElementId idResult = captor.getValue().call();
        
        assertEquals(expectedResult, idResult);
        verify(preparedStatement, times(1)).setString(1, groupAssignation.getType().toString());
        verify(preparedStatement, times(1)).setString(2, groupAssignation.getElementId());
        verify(preparedStatement, times(1)).setLong(3, groupAssignation.getGroupId());
    }
    
}
