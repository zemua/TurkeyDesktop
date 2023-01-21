package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    
    @BeforeClass
    public static void setupClass() {
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
    }
    
    @Before
    public void setup() {
        preparedStatement = mock(PreparedStatement.class);
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
    public void testConfigDbCache() {
        fail();
    }
    
    @Test
    public void testCloseableDbCache() {
        fail();
    }
    
    @Test
    public void testConditionDbCache() {
        fail();
    }
    
}
