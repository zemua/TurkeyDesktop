package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.database.Db;
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

public class GroupRepositoryTest {
    
    Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    GroupFactory factory = mock(GroupFactory.class);
    
    GroupRepository groupRepository;
    
    Group group;
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        groupRepository = new GroupRepository(factory);
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        group = new Group();
        group.setId(0);
        group.setName("my test group");
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet  groupResult = mock(ResultSet.class);
        group.setId(3);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUPS_OF_APPS WHERE ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(groupResult);
        
        groupRepository.findById(group.getId());
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet result = captor.getValue().call();
        
        assertEquals(groupResult, result);
        verify(preparedStatement, times(1)).setLong(1, group.getId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.refEq("INSERT INTO GROUPS_OF_APPS (NAME, TYPE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(Group.ID_COLUMN)).thenReturn(7L);
        
        groupRepository.add(group);
        ArgumentCaptor<Callable<Long>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleLong(captor.capture());
        long longResult = captor.getValue().call();
        
        assertEquals(7L, longResult);
        verify(preparedStatement, times(1)).setString(1, group.getName());
        verify(preparedStatement, times(1)).setString(2, group.getType().toString());
    }
    
    @Test
    public void testAddException() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.refEq("INSERT INTO GROUPS_OF_APPS (NAME, TYPE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(Group.ID_COLUMN)).thenReturn(7L);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        groupRepository.add(group);
        ArgumentCaptor<Callable<Long>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleLong(captor.capture());
        long longResult = captor.getValue().call();
        
        assertEquals(-1L, longResult);
        verify(preparedStatement, times(1)).setString(1, group.getName());
        verify(preparedStatement, times(1)).setString(2, group.getType().toString());
    }
    
}
