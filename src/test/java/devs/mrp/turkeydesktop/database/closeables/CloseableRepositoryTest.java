package devs.mrp.turkeydesktop.database.closeables;

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

public class CloseableRepositoryTest {
    
    static Db db = CommonMocks.getMock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static CloseableRepository closeableRepository;
    
    Closeable closeable;
    
    @BeforeClass
    public static void classSetup() {
        DbFactory.setDbSupplier(() -> db);
        closeableRepository = CloseableRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        closeable = new Closeable();
        closeable.setProcess("test process name");
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet closeableResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM CLOSEABLES_TABLE WHERE PROCESS_NAME=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(closeableResult);
        
        ResultSet result = closeableRepository.findById(closeable.getProcess()).blockingGet();
        assertEquals(closeableResult, result);
        verify(preparedStatement, times(1)).setString(1, closeable.getProcess());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet closeableResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO CLOSEABLES_TABLE (PROCESS_NAME) VALUES (?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(closeableResult);
        
        String result = closeableRepository.add(closeable).blockingGet();
        assertEquals(closeable.getProcess(), result);
        verify(preparedStatement, times(1)).setString(1, closeable.getProcess());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        
        when(db.prepareStatement(ArgumentMatchers.anyString()))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        String result = closeableRepository.add(closeable).blockingGet();
        assertEquals("", result);
        verify(preparedStatement, times(1)).setString(1, closeable.getProcess());
    }
    
}
