package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
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

public class GroupRepositoryTest {
    
    static Db db = CommonMocks.getMock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static GroupRepository groupRepository;
    
    Group group;
    
    @BeforeClass
    public static void classSetup() {
        DbFactory.setDbSupplier(() -> db);
        groupRepository = GroupRepository.getInstance();
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
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet  groupResult = mock(ResultSet.class);
        group.setId(3);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUPS_OF_APPS WHERE ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(groupResult);
        
        ResultSet result = groupRepository.findById(group.getId()).blockingGet();
        assertEquals(groupResult, result);
        verify(preparedStatement, times(1)).setLong(1, group.getId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.refEq("INSERT INTO GROUPS_OF_APPS (NAME, TYPE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(Group.ID_COLUMN)).thenReturn(7L);
        
        long longResult = groupRepository.add(group).blockingGet();
        assertEquals(7L, longResult);
        verify(preparedStatement, times(1)).setString(1, group.getName());
        verify(preparedStatement, times(1)).setString(2, group.getType().toString());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.refEq("INSERT INTO GROUPS_OF_APPS (NAME, TYPE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(Group.ID_COLUMN)).thenReturn(7L);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        long longResult = groupRepository.add(group).blockingGet();
        assertEquals(-1L, longResult);
        verify(preparedStatement, times(1)).setString(1, group.getName());
        verify(preparedStatement, times(1)).setString(2, group.getType().toString());
    }
    
}
