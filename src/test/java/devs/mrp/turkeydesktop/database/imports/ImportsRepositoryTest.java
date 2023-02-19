package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.Connection;
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

public class ImportsRepositoryTest {
    
    Db db = mock(Db.class);
    Connection connection = mock(Connection.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    ImportFactory factory = mock(ImportFactory.class);
    
    ImportsRepository importsRepository;
    
    String testPath = "some/path";
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        importsRepository = new ImportsRepository(factory);
    }
    
    @Before
    public void setup() throws SQLException {
        when(db.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(ArgumentMatchers.matches("SELECT * FROM IMPORTS_TABLE"))).thenReturn(allPreparedStatement);
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM IMPORTS_TABLE WHERE IMPORT_PATH=?")))
                .thenReturn(preparedStatement);
        ResultSet importResult = mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(importResult);
        
        importsRepository.findById(testPath);
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet resultSet = captor.getValue().call();
        
        assertEquals(importResult, resultSet);
        verify(preparedStatement, times(1)).setString(1, testPath);
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO IMPORTS_TABLE (IMPORT_PATH) VALUES (?)")))
                .thenReturn(preparedStatement);
        
        importsRepository.add(testPath);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String result = captor.getValue().call();
        
        assertEquals(testPath, result);
        verify(preparedStatement, times(1)).setString(1, testPath);
    }
    
    @Test
    public void testAddFails() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO IMPORTS_TABLE (IMPORT_PATH) VALUES (?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        importsRepository.add(testPath);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String result = captor.getValue().call();
        
        assertEquals("", result);
        verify(preparedStatement, times(1)).setString(1, testPath);
    }
    
}
