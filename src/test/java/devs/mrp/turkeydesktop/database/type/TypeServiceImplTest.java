package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeServiceImplTest {

    static final DbCache<String,Type> dbCache = mock(DbCache.class);
    static final TypeRepository typeRepository = mock(TypeRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        TypeFactory.setRepoSupplier(() -> typeRepository);
        TypeFactory.setDbCacheSupplier(() -> dbCache);
    }

    @Test
    public void testAddNull() {
        TypeService service = new TypeServiceImpl();
        Type type = null;
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddNullProcess() {
        TypeService service = new TypeServiceImpl();
        Type type = new Type();
        type.setProcess(null);
        type.setType(Type.Types.DEPENDS);
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddEmptyProcess() {
        TypeService service = new TypeServiceImpl();
        Type type = new Type();
        type.setProcess("");
        type.setType(Type.Types.DEPENDS);
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddNullType() {
        TypeService service = new TypeServiceImpl();
        Type type = new Type();
        type.setProcess("process name");
        type.setType(null);
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddSuccess() {
        TypeService service = new TypeServiceImpl();
        Type type = new Type();
        type.setProcess("some process name");
        type.setType(Type.Types.DEPENDS);
        
        when(dbCache.save(type)).thenReturn(Single.just(SaveAction.SAVED));
        
        long saveResult = service.add(type).blockingGet();
        assertEquals(SaveAction.SAVED.get().longValue(), saveResult);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() {
        
        fail();
    }
    
}
