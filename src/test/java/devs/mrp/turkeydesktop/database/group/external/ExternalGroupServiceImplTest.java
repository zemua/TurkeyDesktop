package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalGroupServiceImplTest {
    
    static final Db db = mock(Db.class);
    static final DbCache<Long,ExternalGroup> dbCache = mock(DbCache.class);
    static final ExternalGroupRepository externalGroupRepository = mock(ExternalGroupRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
        ExternalGroupFactory.setDbCacheSupplier(() -> dbCache);
    }

    @Test
    public void testAddNull() {
        ExternalGroupService service = new ExternalGroupServiceImpl();
        ExternalGroup externalGroup = null;
        
        long idResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), idResult);
    }
    
    @Test
    public void testAddInvalidGroup() {
        ExternalGroupService service = new ExternalGroupServiceImpl();
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile("some/path");
        externalGroup.setGroup(0);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddNullFile() {
        ExternalGroupService service = new ExternalGroupServiceImpl();
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile(null);
        externalGroup.setGroup(4);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddEmptyFile() {
        ExternalGroupService service = new ExternalGroupServiceImpl();
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile("");
        externalGroup.setGroup(4);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddTooLongFile() {
        ExternalGroupService service = new ExternalGroupServiceImpl();
        ExternalGroup externalGroup = new ExternalGroup();
        String longString = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        externalGroup.setFile(longString + longString + longString + longString + longString + longString);
        externalGroup.setGroup(4);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddSuccess() {
        ExternalGroupService service = new ExternalGroupServiceImpl();
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile("some/path");
        externalGroup.setGroup(4);
        
        when(dbCache.save(externalGroup)).thenReturn(Single.just(SaveAction.SAVED));
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.SAVED.get().longValue(), saveResult);
    }
    
}
