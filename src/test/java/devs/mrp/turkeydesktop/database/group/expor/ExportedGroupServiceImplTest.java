package devs.mrp.turkeydesktop.database.group.expor;

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
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExportedGroupServiceImplTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final DbCache<ExportedGroupId,ExportedGroup> dbCache = mock(DbCache.class);
    static final ExportedGroupRepository exportedGroupRepository = mock(ExportedGroupRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactoryImpl.setDbSupplier(() -> db);
        ExportedGroupFactoryImpl.setDbCacheSupplier(() -> dbCache);
    }
    
    @Test
    public void testAddNull() {
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup exportedGroup = null;
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddNullPath() {
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile(null);
        exportedGroup.setGroup(2);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddEmptyPath() {
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile("");
        exportedGroup.setGroup(2);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddTooLongPath() {
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        String longString = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        exportedGroup.setFile(longString + longString + longString + longString + longString + longString);
        exportedGroup.setGroup(2);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddInvalidGroup() {
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile("somepath");
        exportedGroup.setGroup(0);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    
    @Test
    public void testAddSuccess() {
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile("somepath");
        exportedGroup.setGroup(2);
        
        when(dbCache.save(exportedGroup)).thenReturn(Single.just(SaveAction.SAVED));
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.SAVED.get(), addResult);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        FactoryInitializer factoryInitializer = new FactoryInitializer();
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
        
        ExportedGroupService service = new ExportedGroupServiceImpl();
        ExportedGroup toBeSaved = new ExportedGroup();
        toBeSaved.setDays(3);
        toBeSaved.setFile("some file to export to");
        toBeSaved.setGroup(4);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getDays(), retrieved.get(0).getDays());
        assertEquals(toBeSaved.getFile(), retrieved.get(0).getFile());
        assertEquals(toBeSaved.getGroup(), retrieved.get(0).getGroup());
    }
    
}
