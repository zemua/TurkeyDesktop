package devs.mrp.turkeydesktop.database.closeables;

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

public class CloseableServiceImplTest {
    
    static final Db db = mock(Db.class);
    static final DbCache<String, Closeable> dbCache = mock(DbCache.class);
    static final CloseableRepository closeableRepository = mock(CloseableRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
        CloseableFactory.setDbCacheSupplier(() -> dbCache);
    }

    @Test
    public void testAddNullKey() {
        CloseableService service = new CloseableServiceImpl();
        String closeable = null;
        
        Long result = service.add(closeable).blockingGet();
        assertEquals(SaveAction.ERROR.get(), result);
    }
    
    @Test
    public void testAddEmptyKey() {
        CloseableService service = new CloseableServiceImpl();
        String closeable = "";
        
        when(dbCache.save(ArgumentMatchers.any(Closeable.class)))
                .thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(closeable).blockingGet();
        assertEquals(SaveAction.ERROR.get(), result);
    }
    
    @Test
    public void testAddSuccess() {
        CloseableService service = new CloseableServiceImpl();
        String closeable = "my closeable process";
        
        when(dbCache.save(ArgumentMatchers.refEq(new Closeable(closeable))))
                .thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(closeable).blockingGet();
        assertEquals(SaveAction.SAVED.get(), result);
    }
    
}
