package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImportServiceImplTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final DbCache<String,String> dbCache = mock(DbCache.class);
    static final ImportsRepository importsRepository = mock(ImportsRepository.class);

    @BeforeClass
    public static void setupClass() {
        DbFactoryImpl.setDbSupplier(() -> db);
        ImportFactory.setDbCacheSupplier(() -> dbCache);
    }
    
    @Test
    public void testSaveInvalidNull() {
        ImportService service = new ImportServiceImpl();
        String path = null;
        
        Long result = service.add(path).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveInvalidEmpty() {
        ImportService service = new ImportServiceImpl();
        String path = "";
        
        Long result = service.add(path).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveSuccess() {
        ImportService service = new ImportServiceImpl();
        String path = "my/valid/path";
        
        when(dbCache.save(ArgumentMatchers.contains(path)))
                .thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(path).blockingGet();
        assertEquals(SaveAction.SAVED.get(), result);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        FactoryInitializer factoryInitializer = new FactoryInitializer();
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
        
        ImportService service = new ImportServiceImpl();
        String toBeAdded = "this is my imported path";
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        service.add(toBeAdded).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertTrue(retrieved.contains("this is my imported path"));
    }
    
}
