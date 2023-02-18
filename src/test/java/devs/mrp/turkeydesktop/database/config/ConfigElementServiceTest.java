package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigElementServiceTest {
    
    Db db = mock(Db.class);
    ConfigElementDao repo = mock(ConfigElementDao.class);
    ConfigElementFactory factory = mock(ConfigElementFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
    }

    @Test
    public void testAddInvalidNull() {
        ConfigElementServiceImpl service = new ConfigElementServiceImpl(factory);
        ConfigElement element = null;
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddNullKey() {
        ConfigElementServiceImpl service = new ConfigElementServiceImpl(factory);
        ConfigElement element = new ConfigElement(null, "some value");
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddNullValue() {
        ConfigElementServiceImpl service = new ConfigElementServiceImpl(factory);
        ConfigElement element = new ConfigElement(ConfigurationEnum.IDLE, null);
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddInvalidTooLongValue() {
        ConfigElementServiceImpl service = new ConfigElementServiceImpl(factory);
        ConfigElement element = new ConfigElement(ConfigurationEnum.IDLE ,"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        ConfigElementServiceImpl service = new ConfigElementServiceImpl(factory);
        ConfigElement element = new ConfigElement(ConfigurationEnum.IDLE, "some short string");
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        
        Long result = service.add(element).blockingGet();
        assertEquals(SaveAction.SAVED.get().longValue(), result.longValue());
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        ConfigElementService service = new ConfigElementServiceImpl(factory);
        ConfigElement toBeSaved = new ConfigElement();
        toBeSaved.setKey(ConfigurationEnum.EXPORT_PATH);
        toBeSaved.setValue("some config value");
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.allConfigElements().toList().blockingGet();
        
        assertEquals(ConfigurationEnum.EXPORT_PATH, retrieved.get(0).getKey());
    }

}
