package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class TitleServiceImplTest {
    
    static Db db = mock(Db.class);
    static DbCache<String,Title> dbCache = mock(DbCache.class);
    static TitleRepository titleRepository = mock(TitleRepository.class);
    static IGroupAssignationService groupAssignationService = mock(IGroupAssignationService.class);
    
    @BeforeClass
    public static void classSetup() {
        TitleFactory.setDbSupplier(() -> db);
        TitleFactory.setDbCacheSupplier(() -> dbCache);
        GroupAssignationFactory.setGroupAssignationServiceSupplier(() -> groupAssignationService);
    }

    @Test
    public void testSaveNullTitle() {
        TitleService service = new TitleServiceImpl();
        Title title = null;
        
        Long result = service.save(title).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveNullSubstring() {
        TitleService service = new TitleServiceImpl();
        Title title = new Title();
        title.setSubStr(null);
        title.setType(Title.Type.POSITIVE);
        
        Long result = service.save(title).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveNullType() {
        TitleService service = new TitleServiceImpl();
        Title title = new Title();
        title.setSubStr("my title");
        title.setType(null);
        
        Long result = service.save(title).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveSuccess() {
        TitleService service = new TitleServiceImpl();
        Title title = new Title();
        title.setSubStr("my title");
        title.setType(Title.Type.POSITIVE);
        
        Long result = service.save(title).blockingGet();
        assertEquals(SaveAction.SAVED, result);
    }
    
}
