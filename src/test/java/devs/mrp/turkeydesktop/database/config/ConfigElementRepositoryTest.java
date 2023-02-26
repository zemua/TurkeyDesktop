package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
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

public class ConfigElementRepositoryTest {
    
    Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    ConfigElementFactory factory = mock(ConfigElementFactory.class);
    
    ConfigElementRepository configRepository;
    
    ConfigElement configElement;
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        configRepository = new ConfigElementRepository(factory);
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        configElement = new ConfigElement();
        configElement.setKey(ConfigurationEnum.IDLE);
        configElement.setValue("1234");
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM CONFIG_TABLE WHERE KEY=?")))
                .thenReturn(preparedStatement);
        ResultSet configResult = mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(configResult);
        
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        configRepository.findById(configElement.getKey().toString());
        verify(db).singleResultSet(captor.capture());
        ResultSet resultSet = captor.getValue().call();
        
        assertEquals(configResult, resultSet);
        verify(preparedStatement, times(1)).setString(1, configElement.getKey().toString());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO CONFIG_TABLE (KEY, VALUE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        
        ArgumentCaptor<Callable> captor = ArgumentCaptor.forClass(Callable.class);
        configRepository.add(configElement);
        verify(db).singleString(captor.capture());
        
        assertEquals(configElement.getKey().toString(), captor.getValue().call());
        verify(preparedStatement, times(1)).setString(1, configElement.getKey().toString());
        verify(preparedStatement, times(1)).setString(2, configElement.getValue());
    }
    
    @Test
    public void testAddException() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO CONFIG_TABLE (KEY, VALUE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        ArgumentCaptor<Callable> captor = ArgumentCaptor.forClass(Callable.class);
        
        configRepository.add(configElement);
        verify(db).singleString(captor.capture());
        
        assertEquals("", captor.getValue().call());
        verify(preparedStatement, times(1)).setString(1, configElement.getKey().toString());
        verify(preparedStatement, times(1)).setString(2, configElement.getValue());
    }
    
}
