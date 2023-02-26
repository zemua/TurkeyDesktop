package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TitleServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<String,Title> dbCache = mock(DbCache.class);
    GroupAssignationService groupAssignationService = mock(GroupAssignationService.class);
    TitleDao repo = mock(TitleDao.class);
    TitleFactory factory = mock(TitleFactory.class);
    
    @Before
    public void setup() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
        when(factory.getGroupAssignationService()).thenReturn(groupAssignationService);
    }
    
    @Test
    public void testSaveNullTitle() {
        TitleService service = new TitleServiceImpl(factory);
        Title title = null;
        
        Long result = service.save(title).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveNullSubstring() {
        TitleService service = new TitleServiceImpl(factory);
        Title title = new Title();
        title.setSubStr(null);
        title.setType(Title.Type.POSITIVE);
        
        Long result = service.save(title).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveNullType() {
        TitleService service = new TitleServiceImpl(factory);
        Title title = new Title();
        title.setSubStr("my title");
        title.setType(null);
        
        Long result = service.save(title).blockingGet();
        assertEquals(-1L, result.longValue());
    }
    
    @Test
    public void testSaveSuccess() {
        TitleService service = new TitleServiceImpl(factory);
        Title title = new Title();
        title.setSubStr("my title");
        title.setType(Title.Type.POSITIVE);
        
        when(dbCache.save(ArgumentMatchers.any(Title.class))).thenReturn(Single.just(SaveAction.SAVED));
        
        Long result = service.save(title).blockingGet();
        assertEquals(SaveAction.SAVED.get(), result);
    }
    
    @Test
    public void testStringIsLowerCased() {
        TitleService service = new TitleServiceImpl(factory);
        Title title = new Title();
        title.setSubStr("My uPPeR CaSeD TiTle");
        title.setType(Title.Type.POSITIVE);
        
        ArgumentCaptor<Title> argumentCaptor = ArgumentCaptor.forClass(Title.class);
        
        when(dbCache.save(ArgumentMatchers.any(Title.class))).thenReturn(Single.just(SaveAction.SAVED));
        
        service.save(title).blockingGet();
        verify(dbCache, atLeast(1)).save(argumentCaptor.capture());
        assertEquals("my upper cased title", argumentCaptor.getValue().getSubStr());
    }
    
    @Test
    public void testFindLongestTitle() {
        TitleService service = new TitleServiceImpl(factory);
        
        Title shortNeutral = new Title("bbb", Title.Type.NEUTRAL);
        Title longNeutral = new Title("bbbbbb", Title.Type.NEUTRAL);
        Title shortNegative = new Title("aaa", Title.Type.NEGATIVE);
        Title longNegative = new Title("aaaaaa", Title.Type.NEGATIVE);
        Title shortPositive = new Title("ccc", Title.Type.POSITIVE);
        Title longPositive = new Title("cccccc", Title.Type.POSITIVE);
        
        String containingString = "aaaaaabbbbbbcccccc";
        
        Observable<Title> obs = Observable.fromIterable(List.of(shortNeutral, longNeutral, shortNegative, longNegative, shortPositive, longPositive));
        when(dbCache.getAll()).thenReturn(obs);
        Maybe<Title> result = service.findLongestContainedBy(containingString);
        assertEquals(longNegative, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(shortNeutral, longNeutral, shortNegative, shortPositive, longPositive));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestContainedBy(containingString);
        assertEquals(longNeutral, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(shortNeutral, shortNegative, shortPositive, longPositive));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestContainedBy(containingString);
        assertEquals(longPositive, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(shortNeutral, shortNegative, shortPositive));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestContainedBy(containingString);
        assertEquals(shortNegative, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(shortNeutral, shortPositive));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestContainedBy(containingString);
        assertEquals(shortNeutral, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(shortPositive));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestContainedBy(containingString);
        assertEquals(shortPositive, result.blockingGet());
        
        obs = Observable.fromIterable(Collections.EMPTY_LIST);
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestContainedBy(containingString);
        assertTrue(result.isEmpty().blockingGet());
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        Title toBeSaved = new Title();
        toBeSaved.setSubStr("My uPPeR CaSeD TiTle");
        toBeSaved.setType(Title.Type.POSITIVE);
        
        Title lowerCased = new Title();
        lowerCased.setSubStr(toBeSaved.getSubStr().toLowerCase());
        lowerCased.setType(Title.Type.POSITIVE);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        ResultSet findAllResultSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllResultSet));
        when(findAllResultSet.next()).thenReturn(false);
        when(repo.add(ArgumentMatchers.refEq(lowerCased))).thenReturn(Single.just(lowerCased.getSubStr()));
        
        TitleFactoryImpl titleFactory = new CacheFactoryTest(repo);
        
        TitleService service = new TitleServiceImpl(titleFactory);
        service.save(toBeSaved).blockingGet();
        
        var retrieved = service.findBySubString(toBeSaved.getSubStr()).blockingGet();
        
        assertEquals("my upper cased title", retrieved.getSubStr());
    }
    
    private class CacheFactoryTest extends TitleFactoryImpl {
        TitleDao repo;
        CacheFactoryTest(TitleDao repo) {
            this.repo = repo;
}
        @Override
        public DbCache<String, Title> getDbCache() {
            return buildCache(repo);
        }
        
        @Override
        public GroupAssignationService getGroupAssignationService() {
            return mock(GroupAssignationService.class);
        }
    }
    
}
