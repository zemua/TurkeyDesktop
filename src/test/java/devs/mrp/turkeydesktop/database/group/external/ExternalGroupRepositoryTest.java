package devs.mrp.turkeydesktop.database.group.external;

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

public class ExternalGroupRepositoryTest {
    
    Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    ExternalGroupFactory factory = mock(ExternalGroupFactory.class);
    ExternalGroupRepository externalGroupRepository;
    
    ExternalGroup externalGroup;
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        externalGroupRepository = new ExternalGroupRepository(factory);
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        externalGroup = new ExternalGroup();
        externalGroup.setFile("some/path");
        externalGroup.setGroup(2);
        externalGroup.setId(0);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet expectedResult = mock(ResultSet.class);
        externalGroup.setId(4);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUPS_EXTERNAL_TABLE WHERE ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(expectedResult);
        
        externalGroupRepository.findById(externalGroup.getId());
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet result = captor.getValue().call();
        
        assertEquals(expectedResult, result);
        verify(preparedStatement, times(1)).setLong(1, externalGroup.getId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        long expectedIdResult = 8L;
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO GROUPS_EXTERNAL_TABLE (GROUP_COLUMN, FILE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(ExternalGroup.ID_COLUMN)).thenReturn(expectedIdResult);
        
        externalGroupRepository.add(externalGroup);
        ArgumentCaptor<Callable<Long>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleLong(captor.capture());
        long idResult = captor.getValue().call();
        
        assertEquals(expectedIdResult, idResult);
        verify(preparedStatement, times(1)).setLong(1, externalGroup.getGroup());
        verify(preparedStatement, times(1)).setString(2, externalGroup.getFile());
    }
    
    @Test
    public void testAddException() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO GROUPS_EXTERNAL_TABLE (GROUP_COLUMN, FILE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        externalGroupRepository.add(externalGroup);
        ArgumentCaptor<Callable<Long>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleLong(captor.capture());
        long idResult = captor.getValue().call();
        
        assertEquals(-1L, idResult);
        verify(preparedStatement, times(1)).setLong(1, externalGroup.getGroup());
        verify(preparedStatement, times(1)).setString(2, externalGroup.getFile());
    }
    
}
