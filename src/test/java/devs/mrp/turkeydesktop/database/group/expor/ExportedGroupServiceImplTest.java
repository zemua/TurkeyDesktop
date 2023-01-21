package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExportedGroupServiceImplTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final DbCache<ExportedGroupId,ExportedGroup> dbCache = mock(DbCache.class);
    static final ExportedGroupRepository exportedGroupRepository = mock(ExportedGroupRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
        ExportedGroupFactory.setDbCacheSupplier(() -> dbCache);
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
    
}
