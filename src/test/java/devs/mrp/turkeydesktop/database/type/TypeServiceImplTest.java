package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeServiceImplTest {

    static final DbCache<String,Type> dbCache = mock(DbCache.class);
    static final TypeRepository typeRepository = mock(TypeRepository.class);
    static final Db db = CommonMocks.getMock(Db.class);
    
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
    public void test_add_sets_object_id_in_cache() throws SQLException {
        FactoryInitializer factoryInitializer = new FactoryInitializer();
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
        
        TypeService service = new TypeServiceImpl();
        Type toBeSaved = new Type();
        toBeSaved.setProcess("some process");
        toBeSaved.setType(Type.Types.DEPENDS);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getType(), retrieved.get(0).getType());
        assertEquals(toBeSaved.getProcess(), retrieved.get(0).getProcess());
    }
    
}
