package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
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

public class ConfigElementRepositoryTest {
    
    static Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static ConfigElementRepository configRepository;
    
    ConfigElement configElement;
    
    @BeforeClass
    public static void classSetup() {
        DbFactory.setDbSupplier(() -> db);
        configRepository = ConfigElementRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        configElement = new ConfigElement();
        configElement.setKey(ConfigurationEnum.IDLE);
        configElement.setValue("1234");
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM CONFIG_TABLE WHERE KEY=?")))
                .thenReturn(preparedStatement);
        ResultSet configResult = mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(configResult);
        
        ResultSet resultSet = configRepository.findById(configElement.getKey().toString()).blockingGet();
        assertEquals(configResult, resultSet);
        verify(preparedStatement, times(1)).setString(1, configElement.getKey().toString());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO CONFIG_TABLE (KEY, VALUE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        
        String result = configRepository.add(configElement).blockingGet();
        assertEquals(configElement.getKey().toString(), result);
        verify(preparedStatement, times(1)).setString(1, configElement.getKey().toString());
        verify(preparedStatement, times(1)).setString(2, configElement.getValue());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO CONFIG_TABLE (KEY, VALUE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        String result = configRepository.add(configElement).blockingGet();
        assertEquals("", result);
        verify(preparedStatement, times(1)).setString(1, configElement.getKey().toString());
        verify(preparedStatement, times(1)).setString(2, configElement.getValue());
    }
    
}
