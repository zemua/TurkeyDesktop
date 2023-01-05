package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;

public class TitleServiceImplTest {
    
    static final Db db = mock(Db.class);
    static final DbCache<String,Title> dbCache = mock(DbCache.class);
    static final GroupAssignationService groupAssignationService = mock(GroupAssignationService.class);
    
    @BeforeClass
    public static void setup() {
        DbFactory.setDbSupplier(() -> db);
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
        
        when(dbCache.save(ArgumentMatchers.any(Title.class))).thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.save(title).blockingGet();
        assertEquals(SaveAction.SAVED.get(), result);
    }
    
    @Test
    public void testStringIsLowerCased() {
        TitleService service = new TitleServiceImpl();
        Title title = new Title();
        title.setSubStr("My uPPeR CaSeD TiTle");
        title.setType(Title.Type.POSITIVE);
        
        ArgumentCaptor<Title> argumentCaptor = ArgumentCaptor.forClass(Title.class);
        
        when(dbCache.save(ArgumentMatchers.any(Title.class))).thenReturn(Single.just(SaveAction.SAVED));
        
        service.save(title).blockingGet();
        verify(dbCache, atLeast(1)).save(argumentCaptor.capture());
        assertEquals("my upper cased title", argumentCaptor.getValue().getSubStr());
    }
    
}
