package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.closeables.Closeable;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactory;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactory;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactory;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
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
        var result = DbFactory.getDb();
        
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
        var cache = ConfigElementFactory.getDbCache();
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
        
        var cache = CloseableFactory.getDbCache();
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
        var cache = ConditionFactory.getDbCache();
        Condition expected = new Condition();
        expected.setGroupId(3);
        expected.setLastDaysCondition(0);
        expected.setTargetId(6);
        expected.setUsageTimeCondition(123456);
        
        cache.save(expected).blockingGet();
        var result = cache.read(9L).blockingGet();
        
        assertEquals(expected, result);
    }
    
}
