package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupAssignationServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<GroupAssignationDao.ElementId, GroupAssignation> dbCache = mock(DbCache.class);
    GroupAssignationRepository repo = mock(GroupAssignationRepository.class);
    
    GroupAssignationFactory factory = mock(GroupAssignationFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
    }

    @Test
    public void testAddNullAssignation() {
        GroupAssignationService service = new GroupAssignationServiceImpl(factory);
        GroupAssignation assignation = null;
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithNullType() {
        GroupAssignationService service = new GroupAssignationServiceImpl(factory);
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("some title");
        assignation.setGroupId(3);
        assignation.setType(null);
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithInvalidElementName() {
        GroupAssignationService service = new GroupAssignationServiceImpl(factory);
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("");
        assignation.setGroupId(3);
        assignation.setType(GroupAssignation.ElementType.TITLE);
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithInvalidGroupId() {
        GroupAssignationService service = new GroupAssignationServiceImpl(factory);
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("some title");
        assignation.setGroupId(0);
        assignation.setType(GroupAssignation.ElementType.TITLE);
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddSuccess() {
        GroupAssignationService service = new GroupAssignationServiceImpl(factory);
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("some title");
        assignation.setGroupId(3);
        assignation.setType(GroupAssignation.ElementType.TITLE);
        
        when(dbCache.save(assignation)).thenReturn(Single.just(SaveAction.SAVED));
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.SAVED.get(), addResult);
    }
    
    @Test
    public void testFindLongestTitle() {
        GroupAssignationService service = new GroupAssignationServiceImpl(factory);
        
        GroupAssignation length1 = new GroupAssignation(GroupAssignation.ElementType.TITLE, "b", 1);
        GroupAssignation length2 = new GroupAssignation(GroupAssignation.ElementType.TITLE, "bb", 1);
        GroupAssignation length3 = new GroupAssignation(GroupAssignation.ElementType.TITLE, "bbb", 1);
        GroupAssignation length4 = new GroupAssignation(GroupAssignation.ElementType.TITLE, "bbbb", 1);
        GroupAssignation length5 = new GroupAssignation(GroupAssignation.ElementType.TITLE, "bbbbb", 1);
        GroupAssignation notTitle = new GroupAssignation(GroupAssignation.ElementType.PROCESS, "bbbbbb", 1);
        
        String containingString = "bbbbbb";
        
        Observable<GroupAssignation> obs = Observable.fromIterable(List.of(length4, length5, notTitle, length1, length2, length3));
        when(dbCache.getAll()).thenReturn(obs);
        Maybe<GroupAssignation> result = service.findLongestTitleIdContainedIn(containingString);
        assertEquals(length5, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(length4, notTitle, length1, length2, length3));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestTitleIdContainedIn(containingString);
        assertEquals(length4, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(notTitle, length1, length2, length3));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestTitleIdContainedIn(containingString);
        assertEquals(length3, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(notTitle, length1, length2));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestTitleIdContainedIn(containingString);
        assertEquals(length2, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(notTitle, length1));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestTitleIdContainedIn(containingString);
        assertEquals(length1, result.blockingGet());
        
        obs = Observable.fromIterable(List.of(notTitle));
        when(dbCache.getAll()).thenReturn(obs);
        result = service.findLongestTitleIdContainedIn(containingString);
        assertTrue(result.isEmpty().blockingGet());
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        GroupAssignation toBeSaved = new GroupAssignation();
        toBeSaved.setElementId("process or title");
        toBeSaved.setGroupId(4);
        toBeSaved.setType(GroupAssignation.ElementType.TITLE);
        var id = new GroupAssignationDao.ElementId(toBeSaved.getType(), toBeSaved.getElementId());
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        ResultSet findAllResultSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllResultSet));
        when(findAllResultSet.next()).thenReturn(false);
        when(repo.add(toBeSaved)).thenReturn(Single.just(id));
        
        GroupAssignationFactoryImpl gaFactory = new CacheFactoryTest(repo);
        
        GroupAssignationService service = new GroupAssignationServiceImpl(gaFactory);
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getType(), retrieved.get(0).getType());
        assertEquals(toBeSaved.getElementId(), retrieved.get(0).getElementId());
        assertEquals(toBeSaved.getGroupId(), retrieved.get(0).getGroupId());
    }
    
    private class CacheFactoryTest extends GroupAssignationFactoryImpl {
        GroupAssignationDao repo;
        CacheFactoryTest(GroupAssignationDao repo) {
            this.repo = repo;
        }
        @Override
        public DbCache<GroupAssignationDao.ElementId, GroupAssignation> getDbCache() {
            return buildCache(repo);
        }
        
        @Override
        public GroupAssignationService getService() {
            return mock(GroupAssignationService.class);
        }
    }
    
}
