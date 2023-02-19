package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.closeables.Closeable;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationServiceImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupId;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.TypeServiceImpl;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FactoryInitializerTest {
    
    Db db;
    DbFactory dbFactory;
    ResultSet allResultSet;
    PreparedStatement preparedStatement;
    ResultSet generatedKeysResultSet;
    
    FactoryInitializer factoryInitializer;
    
    @Before
    public void setupClass() throws SQLException {
        db = mock(Db.class);
        dbFactory = mock(DbFactory.class);
        allResultSet = mock(ResultSet.class);
        preparedStatement = mock(PreparedStatement.class);
        generatedKeysResultSet = mock(ResultSet.class);
        
        when(dbFactory.getDb()).thenReturn(db);
        when(db.singleResultSet(ArgumentMatchers.any())).thenReturn(Single.just(allResultSet));
        when(allResultSet.next()).thenReturn(Boolean.FALSE);
        factoryInitializer = FactoryInitializer.getNew(dbFactory);
    }
    
    @Test
    public void testInitGeneralDb() {
        var result = factoryInitializer.getDbFactory();
        
        assertEquals(dbFactory, result);
    }

    @Test
    public void testTitleDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(db.singleString(ArgumentMatchers.any())).thenReturn(Single.just("my title"));
        
        DbCache<String, Title> dbCache = factoryInitializer.getTitleFactory().getDbCache();
        Title title = new Title();
        title.setSubStr("my title");
        title.setType(Title.Type.POSITIVE);
        
        dbCache.save(title).blockingGet();
        Title result = dbCache.read(title.getSubStr()).blockingGet();
        
        assertEquals(title, result);
    }
    
    @Test
    public void testImportsDbCache() throws SQLException {
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(db.singleString(ArgumentMatchers.any())).thenReturn(Single.just("some import"));
        
        var cache = factoryInitializer.getImportFactory().getDbCache();
        String expected = "some import";
        
        cache.save(expected).blockingGet();
        var result = cache.read(expected).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCloseableDbCache() throws SQLException {
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(db.singleString(ArgumentMatchers.any())).thenReturn(Single.just("some process"));
        
        var cache = factoryInitializer.getCloseableFactory().getDbCache();
        Closeable expected = new Closeable();
        expected.setProcess("some process");
        
        cache.save(expected).blockingGet();
        var result = cache.read(expected.getProcess()).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testConditionDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(db.singleLong(ArgumentMatchers.any())).thenReturn(Single.just(9L));
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeysResultSet);
        when(generatedKeysResultSet.next()).thenReturn(Boolean.TRUE);
        when(generatedKeysResultSet.getLong(ArgumentMatchers.anyInt())).thenReturn(9L);
        
        var cache = factoryInitializer.getConditionFactory().getDbCache();
        Condition expected = new Condition();
        expected.setGroupId(3);
        expected.setLastDaysCondition(0);
        expected.setTargetId(6);
        expected.setUsageTimeCondition(123456);
        
        cache.save(expected).blockingGet();
        var result = cache.read(9L).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testExportedGroupDbCache() throws SQLException {
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(preparedStatement);
        
        var cache = factoryInitializer.getExportedGroupFactory().getDbCache();
        ExportedGroup expected = new ExportedGroup();
        expected.setDays(4);
        expected.setFile("some file path");
        expected.setGroup(7);
        ExportedGroupId expectedId = new ExportedGroupId(expected.getGroup(), expected.getFile());
        
        when(db.singleGeneric(ArgumentMatchers.any())).thenReturn(Single.just(expectedId));
        
        cache.save(expected).blockingGet();
        var result = cache.read(expectedId).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testExternalGroupDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(db.singleLong(ArgumentMatchers.any())).thenReturn(Single.just(14L));
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeysResultSet);
        when(generatedKeysResultSet.next()).thenReturn(Boolean.TRUE);
        when(generatedKeysResultSet.getLong(ArgumentMatchers.anyInt())).thenReturn(14L);
        
        var cache = factoryInitializer.getExternalGroupFactory().getDbCache();
        ExternalGroup expected = new ExternalGroup();
        expected.setFile("some file");
        expected.setGroup(6);
        
        cache.save(expected).blockingGet();
        var result = cache.read(14L).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFactoryDb() {
        assertEquals(db, factoryInitializer.getTypeFactory().getDb());
    }
    
    @Test
    public void testTypeRepo() {
        assertTrue(factoryInitializer.getTypeFactory().getService() instanceof TypeServiceImpl);
    }
    
    @Test
    public void testGroupAssignationService() {
        var result = factoryInitializer.getGroupAssignationFactory().getService();
        assertTrue(result instanceof GroupAssignationServiceImpl);
    }
    
}
