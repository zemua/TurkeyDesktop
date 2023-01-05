package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImportServiceImplTest {
    
    static final Db db = mock(Db.class);
    static final DbCache<String,String> dbCache = mock(DbCache.class);
    static final ImportsRepository importsRepository = mock(ImportsRepository.class);

    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
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
    
}
