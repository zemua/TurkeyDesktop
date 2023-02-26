package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
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
