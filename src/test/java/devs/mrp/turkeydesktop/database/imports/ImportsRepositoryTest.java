package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import java.sql.Connection;
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

public class ImportsRepositoryTest {
    
    static Db db = CommonMocks.getMock(Db.class);
    Connection connection = mock(Connection.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static ImportsRepository importsRepository;
    
    String testPath = "some/path";
    
    @BeforeClass
    public static void classSetup() {
        DbFactory.setDbSupplier(() -> db);
        importsRepository = ImportsRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(db.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(ArgumentMatchers.matches("SELECT * FROM IMPORTS_TABLE"))).thenReturn(allPreparedStatement);
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM IMPORTS_TABLE WHERE IMPORT_PATH=?")))
                .thenReturn(preparedStatement);
        ResultSet importResult = mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(importResult);
        
        ResultSet resultSet = importsRepository.findById(testPath).blockingGet();
        assertEquals(importResult, resultSet);
        verify(preparedStatement, times(1)).setString(1, testPath);
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO IMPORTS_TABLE (IMPORT_PATH) VALUES (?)")))
                .thenReturn(preparedStatement);
        
        String result = importsRepository.add(testPath).blockingGet();
        assertEquals(testPath, result);
        verify(preparedStatement, times(1)).setString(1, testPath);
    }
    
    @Test
    public void testAddFails() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO IMPORTS_TABLE (IMPORT_PATH) VALUES (?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        String result = importsRepository.add(testPath).blockingGet();
        assertEquals("", result);
        verify(preparedStatement, times(1)).setString(1, testPath);
    }
    
}
