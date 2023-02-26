package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImportServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<String,String> dbCache = mock(DbCache.class);
    ImportsRepository repo = mock(ImportsRepository.class);
    ImportFactory factory = mock(ImportFactory.class);

    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
    }
    
    @Test
    public void testSaveInvalidNull() {
        ImportService service = new ImportServiceImpl(factory);
        String path = null;
        
        Long result = service.add(path).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveInvalidEmpty() {
        ImportService service = new ImportServiceImpl(factory);
        String path = "";
        
        Long result = service.add(path).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveSuccess() {
        ImportService service = new ImportServiceImpl(factory);
        String path = "my/valid/path";
        
        when(dbCache.save(ArgumentMatchers.contains(path)))
                .thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.add(path).blockingGet();
        assertEquals(SaveAction.SAVED.get(), result);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        String toBeSaved = "this is my imported path";
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        ResultSet findAllResultSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllResultSet));
        when(findAllResultSet.next()).thenReturn(false);
        when(repo.add(toBeSaved)).thenReturn(Single.just(toBeSaved));
        
        ImportFactoryImpl importFactory = new CacheFactoryTest(repo);
        
        ImportService service = new ImportServiceImpl(importFactory);
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertTrue(retrieved.contains("this is my imported path"));
    }
    
    private class CacheFactoryTest extends ImportFactoryImpl {
        ImportsDao repo;
        CacheFactoryTest(ImportsDao repo) {
            this.repo = repo;
        }
        @Override
        public DbCache<String, String> getDbCache() {
            return buildCache(repo);
        }
    }
    
}
