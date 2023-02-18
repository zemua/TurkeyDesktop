package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CloseableServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<String, Closeable> dbCache = mock(DbCache.class);
    CloseableRepository closeableRepository = mock(CloseableRepository.class);
    
    CloseableFactory factory = mock(CloseableFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
    }

    @Test
    public void testAddNullKey() {
        CloseableService service = new CloseableServiceImpl(factory);
        String closeable = null;
        
        Long result = service.add(closeable).blockingGet();
        assertEquals(SaveAction.ERROR.get(), result);
    }
    
    @Test
    public void testAddEmptyKey() {
        CloseableService service = new CloseableServiceImpl(factory);
        String closeable = "";
        
        when(dbCache.save(ArgumentMatchers.any(Closeable.class)))
                .thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(closeable).blockingGet();
        assertEquals(SaveAction.ERROR.get(), result);
    }
    
    @Test
    public void testAddSuccess() {
        CloseableService service = new CloseableServiceImpl(factory);
        String closeable = "my closeable process";
        
        when(dbCache.save(ArgumentMatchers.refEq(new Closeable(closeable))))
                .thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(closeable).blockingGet();
        assertEquals(SaveAction.SAVED.get(), result);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        CloseableService service = new CloseableServiceImpl(factory);
        Closeable toBeSaved = new Closeable();
        toBeSaved.setProcess("process closeable name");
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        service.add(toBeSaved.getProcess()).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getProcess(), retrieved.get(0).getProcess());
    }
    
}
