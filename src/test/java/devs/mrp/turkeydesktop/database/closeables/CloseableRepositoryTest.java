package devs.mrp.turkeydesktop.database.closeables;

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

public class CloseableRepositoryTest {
    
    Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    CloseableFactory closeableFactory = mock(CloseableFactory.class);
    
    CloseableRepository closeableRepository;
    Closeable closeable;
    
    @Before
    public void classSetup() {
        when(closeableFactory.getDb()).thenReturn(db);
        closeableRepository = new CloseableRepository(closeableFactory);
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        closeable = new Closeable();
        closeable.setProcess("test process name");
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet closeableResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM CLOSEABLES_TABLE WHERE PROCESS_NAME=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(closeableResult);
        
        closeableRepository.findById(closeable.getProcess());
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet result = captor.getValue().call();
        
        assertEquals(closeableResult, result);
        verify(preparedStatement, times(1)).setString(1, closeable.getProcess());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet closeableResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO CLOSEABLES_TABLE (PROCESS_NAME) VALUES (?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(closeableResult);
        
        closeableRepository.add(closeable);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String result = captor.getValue().call();
        
        assertEquals(closeable.getProcess(), result);
        verify(preparedStatement, times(1)).setString(1, closeable.getProcess());
    }
    
    @Test
    public void testAddException() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        
        when(db.prepareStatement(ArgumentMatchers.anyString()))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        closeableRepository.add(closeable);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String result = captor.getValue().call();
        
        assertEquals("", result);
        verify(preparedStatement, times(1)).setString(1, closeable.getProcess());
    }
    
}
