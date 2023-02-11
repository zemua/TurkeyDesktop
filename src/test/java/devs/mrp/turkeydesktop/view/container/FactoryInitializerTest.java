package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.closeables.Closeable;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactoryImpl;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationServiceImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupId;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupFactory;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeFactory;
import devs.mrp.turkeydesktop.database.type.TypeRepository;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FactoryInitializerTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final FactoryInitializer factoryInitializer = new FactoryInitializer();
    
    private PreparedStatement preparedStatement;
    private ResultSet generatedKeysResultSet;
    
    @BeforeClass
    public static void setupClass() {
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
    }
    
    @Before
    public void setup() {
        preparedStatement = mock(PreparedStatement.class);
        generatedKeysResultSet = mock(ResultSet.class);
    }
    
    @Test
    public void testInitGeneralDb() {
        var result = DbFactoryImpl.getDb();
        
        assertEquals(db, result);
    }

    @Test
    public void testTitleDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        
        DbCache<String, Title> dbCache = TitleFactory.getDbCache();
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
        
        var cache = ImportFactory.getDbCache();
        String expected = "some import";
        
        cache.save(expected).blockingGet();
        var result = cache.read(expected).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testConfigDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        var cache = factoryInitializer.getConfigElementFactory().getDbCache();
        ConfigElement expected = new ConfigElement();
        expected.setKey(ConfigurationEnum.IDLE);
        expected.setValue("some value");
        
        cache.save(expected).blockingGet();
        var result = cache.read(expected.getKey().toString()).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCloseableDbCache() throws SQLException {
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(preparedStatement);
        
        var cache = CloseableFactoryImpl.getDbCache();
        Closeable expected = new Closeable();
        expected.setProcess("some process");
        
        cache.save(expected).blockingGet();
        var result = cache.read(expected.getProcess()).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testConditionDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeysResultSet);
        when(generatedKeysResultSet.next()).thenReturn(Boolean.TRUE);
        when(generatedKeysResultSet.getLong(ArgumentMatchers.anyInt())).thenReturn(9L);
        var cache = ConditionFactoryImpl.getDbCache();
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
        var cache = ExportedGroupFactoryImpl.getDbCache();
        ExportedGroup expected = new ExportedGroup();
        expected.setDays(4);
        expected.setFile("some file path");
        expected.setGroup(7);
        ExportedGroupId expectedId = new ExportedGroupId(expected.getGroup(), expected.getFile());
        
        cache.save(expected).blockingGet();
        var result = cache.read(expectedId).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testExternalGroupDbCache() throws SQLException {
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeysResultSet);
        when(generatedKeysResultSet.next()).thenReturn(Boolean.TRUE);
        when(generatedKeysResultSet.getLong(ArgumentMatchers.anyInt())).thenReturn(14L);
        var cache = ExternalGroupFactory.getDbCache();
        ExternalGroup expected = new ExternalGroup();
        expected.setFile("some file");
        expected.setGroup(6);
        
        cache.save(expected).blockingGet();
        var result = cache.read(14L).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeFactoryDb() {
        assertEquals(db, TypeFactory.getDb());
    }
    
    @Test
    public void testTypeDbCache() throws SQLException {
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(preparedStatement);
        var cache = TypeFactory.getDbCache();
        Type expected = new Type();
        expected.setProcess("some process");
        expected.setType(Type.Types.DEPENDS);
        
        cache.save(expected).blockingGet();
        var result = cache.read(expected.getProcess()).blockingGet();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTypeRepo() {
        assertEquals(TypeRepository.getInstance(), TypeFactory.getTypeRepo());
    }
    
    @Test
    public void testGroupAssignationService() {
        var result = GroupAssignationFactoryImpl.getService();
        assertTrue(result instanceof GroupAssignationServiceImpl);
    }
    
}
