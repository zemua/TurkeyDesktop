package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.titles.TitleServiceImpl;
import java.util.List;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class FactoryInitializerTest {
    
    static final Db db = mock(Db.class);
    static final FactoryInitializer factoryInitializer = new FactoryInitializer();
    
    @BeforeClass
    public static void setupClass() {
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
    }

    @Test
    public void testTitleDbCache() {
        DbCache<String, Title> dbCache = TitleFactory.getDbCache();
        TitleService service = new TitleServiceImpl();
        List<Title> titles = service.findAll().toList().blockingGet();
        
        fail();
    }
    
    @Test
    public void testImportsDbCache() {
        fail();
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
