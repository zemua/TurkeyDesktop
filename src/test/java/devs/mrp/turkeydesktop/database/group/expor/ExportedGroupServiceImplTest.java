package devs.mrp.turkeydesktop.database.group.expor;

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

public class ExportedGroupServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<ExportedGroupId,ExportedGroup> dbCache = mock(DbCache.class);
    ExportedGroupRepository repo = mock(ExportedGroupRepository.class);
    
    ExportedGroupFactory factory = mock(ExportedGroupFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
    }
    
    @Test
    public void testAddNull() {
        ExportedGroupService service = new ExportedGroupServiceImpl(factory);
        ExportedGroup exportedGroup = null;
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddNullPath() {
        ExportedGroupService service = new ExportedGroupServiceImpl(factory);
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile(null);
        exportedGroup.setGroup(2);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddEmptyPath() {
        ExportedGroupService service = new ExportedGroupServiceImpl(factory);
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile("");
        exportedGroup.setGroup(2);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddTooLongPath() {
        ExportedGroupService service = new ExportedGroupServiceImpl(factory);
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
        ExportedGroupService service = new ExportedGroupServiceImpl(factory);
        ExportedGroup exportedGroup = new ExportedGroup();
        exportedGroup.setDays(0);
        exportedGroup.setFile("somepath");
        exportedGroup.setGroup(0);
        
        Long addResult = service.add(exportedGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    
    @Test
    public void testAddSuccess() {
        ExportedGroupService service = new ExportedGroupServiceImpl(factory);
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
        ExportedGroup toBeSaved = new ExportedGroup();
        toBeSaved.setDays(3);
        toBeSaved.setFile("some file to export to");
        toBeSaved.setGroup(4);
        var id = new ExportedGroupId(toBeSaved.getGroup(), toBeSaved.getFile());
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        ResultSet findAllResultSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllResultSet));
        when(findAllResultSet.next()).thenReturn(Boolean.FALSE);
        when(repo.add(toBeSaved)).thenReturn(Single.just(id));
        
        ExportedGroupFactoryImpl egFactory = new CacheFactoryTest(repo);
        
        ExportedGroupService service = new ExportedGroupServiceImpl(egFactory);
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getDays(), retrieved.get(0).getDays());
        assertEquals(toBeSaved.getFile(), retrieved.get(0).getFile());
        assertEquals(toBeSaved.getGroup(), retrieved.get(0).getGroup());
    }
    
    private class CacheFactoryTest extends ExportedGroupFactoryImpl {
        ExportedGroupDao repo;
        CacheFactoryTest(ExportedGroupDao repo) {
            this.repo = repo;
        }
        @Override
        public DbCache<ExportedGroupId,ExportedGroup> getDbCache() {
            return buildCache(repo);
        }
        
        @Override
        public ExportedGroupService getService() {
            return mock(ExportedGroupService.class);
        }
    }
    
}
