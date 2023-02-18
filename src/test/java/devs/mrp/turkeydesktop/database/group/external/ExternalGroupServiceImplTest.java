package devs.mrp.turkeydesktop.database.group.external;

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

public class ExternalGroupServiceImplTest {
    
    Db db = mock(Db.class);
    DbCache<Long,ExternalGroup> dbCache = mock(DbCache.class);
    ExternalGroupRepository externalGroupRepository = mock(ExternalGroupRepository.class);
    ExternalGroupFactory factory = mock(ExternalGroupFactory.class);
    
    @Before
    public void setupClass() {
        when(factory.getDb()).thenReturn(db);
        when(factory.getDbCache()).thenReturn(dbCache);
    }

    @Test
    public void testAddNull() {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup externalGroup = null;
        
        long idResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), idResult);
    }
    
    @Test
    public void testAddInvalidGroup() {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile("some/path");
        externalGroup.setGroup(0);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddNullFile() {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile(null);
        externalGroup.setGroup(4);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddEmptyFile() {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile("");
        externalGroup.setGroup(4);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddTooLongFile() {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup externalGroup = new ExternalGroup();
        String longString = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        externalGroup.setFile(longString + longString + longString + longString + longString + longString);
        externalGroup.setGroup(4);
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.ERROR.get().longValue(), saveResult);
    }
    
    @Test
    public void testAddSuccess() {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setFile("some/path");
        externalGroup.setGroup(4);
        
        when(dbCache.save(externalGroup)).thenReturn(Single.just(SaveAction.SAVED));
        
        long saveResult = service.add(externalGroup).blockingGet();
        assertEquals(SaveAction.SAVED.get().longValue(), saveResult);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        ExternalGroupService service = new ExternalGroupServiceImpl(factory);
        ExternalGroup toBeSaved = new ExternalGroup();
        toBeSaved.setId(0);
        toBeSaved.setFile("some file to export");
        toBeSaved.setGroup(4);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(ExternalGroup.ID_COLUMN)).thenReturn(879L);
        
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(879, retrieved.get(0).getId());
    }
    
}
