package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
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

public class GroupServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<Long, Group> dbCache = mock(DbCache.class);
    GroupRepository repo = mock(GroupRepository.class);
    
    GroupFactory factory = mock(GroupFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
    }

    @Test
    public void testAddNullGroup() {
        GroupService service = new GroupServiceImpl(factory);
        Group group = null;
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithBlankName() {
        GroupService service = new GroupServiceImpl(factory);
        Group group = new Group();
        group.setName("");
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithNullName() {
        GroupService service = new GroupServiceImpl(factory);
        Group group = new Group();
        group.setName(null);
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithNullType() {
        GroupService service = new GroupServiceImpl(factory);
        Group group = new Group();
        group.setName("some name");
        group.setPreventClose(true);
        group.setType(null);
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddSuccess() {
        GroupService service = new GroupServiceImpl(factory);
        Group group = new Group();
        group.setName("some name");
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
        
        when(dbCache.save(group)).thenReturn(Single.just(SaveAction.SAVED));
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.SAVED.get(), addResult);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {        
        Group toBeSaved = new Group();
        toBeSaved.setId(0);
        toBeSaved.setName("some name");
        toBeSaved.setPreventClose(true);
        toBeSaved.setType(Group.GroupType.POSITIVE);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(Group.ID_COLUMN)).thenReturn(879L);
        
        FactoryInitializer initializer = mock(FactoryInitializer.class);
        DbFactory dbfactory = mock(DbFactory.class);
        when(initializer.getDbFactory()).thenReturn(dbfactory);
        when(dbfactory.getDb()).thenReturn(db);
        ResultSet findAllResultSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllResultSet));
        when(findAllResultSet.next()).thenReturn(false);
        when(repo.add(toBeSaved)).thenReturn(Single.just(879L));
        
        DbCache<Long, Group> cache = new CacheFactoryTest(initializer, repo).getDbCache();
        when(factory.getDbCache()).thenReturn(cache);
        
        GroupService service = new GroupServiceImpl(factory);
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(879, retrieved.get(0).getId());
    }
    
    private class CacheFactoryTest extends GroupFactoryImpl {
        GroupDao repo;
        CacheFactoryTest(FactoryInitializer factory, GroupDao repo) {
            super(factory);
            this.repo = repo;
        }
        @Override
        public DbCache<Long, Group> getDbCache() {
            return buildCache(repo);
        }
    }
    
}
