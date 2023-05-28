package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeServiceImplTest {

    DbCache<String,Type> dbCache = mock(DbCache.class);
    TypeRepository repo = mock(TypeRepository.class);
    Db db = mock(Db.class);
    
    TypeFactory factory = mock(TypeFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDbCache()).thenReturn(dbCache);
    }

    @Test
    public void testAddNull() {
        TypeService service = new TypeServiceImpl(factory);
        Type type = null;
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddNullProcess() {
        TypeService service = new TypeServiceImpl(factory);
        Type type = new Type();
        type.setProcess(null);
        type.setType(Type.Types.DEPENDS);
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddEmptyProcess() {
        TypeService service = new TypeServiceImpl(factory);
        Type type = new Type();
        type.setProcess("");
        type.setType(Type.Types.DEPENDS);
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddNullType() {
        TypeService service = new TypeServiceImpl(factory);
        Type type = new Type();
        type.setProcess("process name");
        type.setType(null);
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddSuccess() {
        TypeService service = new TypeServiceImpl(factory);
        Type type = new Type();
        type.setProcess("some process name");
        type.setType(Type.Types.DEPENDS);
        
        when(dbCache.save(type)).thenReturn(Single.just(SaveAction.SAVED));
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.SAVED.get().longValue(), saveResult);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        Type toBeSaved = new Type();
        toBeSaved.setProcess("some Process");
        toBeSaved.setType(Type.Types.DEPENDS);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        ResultSet findAllResultSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllResultSet));
        when(findAllResultSet.next()).thenReturn(false);
        when(repo.add(toBeSaved)).thenReturn(Single.just(toBeSaved.getProcess()));
        
        TypeFactoryImpl typeFactory = new CacheFactoryTest(repo);
        
        TypeService service = new TypeServiceImpl(typeFactory);
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getType(), retrieved.get(0).getType());
        assertEquals(toBeSaved.getProcess(), retrieved.get(0).getProcess());
    }
    
    private class CacheFactoryTest extends TypeFactoryImpl {
        TypeDao repo;
        CacheFactoryTest(TypeDao repo) {
            this.repo = repo;
        }
        @Override
        public DbCache<String, Type> getDbCache() {
            return buildCache(repo);
        }
        
        @Override
        public Db getDb() {
            return db;
        }
        
        @Override
        public GroupAssignationService getGroupAssignationService() {
            return mock(GroupAssignationService.class);
        }
    }
    
}
