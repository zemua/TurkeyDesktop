package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigElementServiceTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final DbCache<String, ConfigElement> dbCache = mock(DbCache.class);
    static final ConfigElementRepository configRepository = mock(ConfigElementRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
        ConfigElementFactory.setDbCacheSupplier(() -> dbCache);
    }

    @Test
    public void testAddInvalidNull() {
        ConfigElementServiceImplementation service = new ConfigElementServiceImplementation();
        ConfigElement element = null;
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddNullKey() {
        ConfigElementServiceImplementation service = new ConfigElementServiceImplementation();
        ConfigElement element = new ConfigElement(null, "some value");
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddNullValue() {
        ConfigElementServiceImplementation service = new ConfigElementServiceImplementation();
        ConfigElement element = new ConfigElement(ConfigurationEnum.IDLE, null);
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddInvalidTooLongValue() {
        ConfigElementServiceImplementation service = new ConfigElementServiceImplementation();
        ConfigElement element = new ConfigElement(ConfigurationEnum.IDLE ,"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        
        Long result = service.add(element).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testAddSuccess() {
        ConfigElementServiceImplementation service = new ConfigElementServiceImplementation();
        ConfigElement element = new ConfigElement(ConfigurationEnum.IDLE, "some short string");
        
        when(dbCache.save(element)).thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(element).blockingGet();
        assertEquals(SaveAction.SAVED.get().longValue(), result.longValue());
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() {
        
        fail();
    }

}
