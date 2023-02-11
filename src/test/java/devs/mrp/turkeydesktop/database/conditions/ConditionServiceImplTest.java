package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConditionServiceImplTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final DbCache<Long, Condition> dbCache = mock(DbCache.class);
    static final ConditionRepository conditionRepository = mock(ConditionRepository.class);

    @BeforeClass
    public static void setupClass() {
        DbFactoryImpl.setDbSupplier(() -> db);
        ConditionFactoryImpl.setDbCacheSupplier(() -> dbCache);
    }
    
    @Test
    public void testAddNullCondition() {
        ConditionService service = new ConditionServiceImpl();
        Condition condition = null;
        
        Long saveResult = service.add(condition).blockingGet();
        assertEquals(SaveAction.ERROR.get(), saveResult);
    }
    
    @Test
    public void testAddInvalidGroup() {
        ConditionService service = new ConditionServiceImpl();
        Condition condition = new Condition();
        condition.setGroupId(0);
        condition.setLastDaysCondition(3);
        condition.setTargetId(2);
        condition.setUsageTimeCondition(123456);
        
        Long saveResult = service.add(condition).blockingGet();
        assertEquals(SaveAction.ERROR.get(), saveResult);
    }
    
    @Test
    public void testAddInvalidTarget() {
        ConditionService service = new ConditionServiceImpl();
        Condition condition = new Condition();
        condition.setGroupId(2);
        condition.setLastDaysCondition(3);
        condition.setTargetId(0);
        condition.setUsageTimeCondition(123456);
        
        Long saveResult = service.add(condition).blockingGet();
        assertEquals(SaveAction.ERROR.get(), saveResult);
    }
    
    @Test
    public void testAddSuccess() {
        ConditionService service = new ConditionServiceImpl();
        Condition condition = new Condition();
        condition.setGroupId(2);
        condition.setLastDaysCondition(3);
        condition.setTargetId(1);
        condition.setUsageTimeCondition(123456);
        
        when(dbCache.save(ArgumentMatchers.refEq(condition))).thenReturn(Single.just(SaveAction.SAVED));
        
        Long saveResult = service.add(condition).blockingGet();
        assertEquals(SaveAction.SAVED.get(), saveResult);
    }
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        FactoryInitializer factoryInitializer = new FactoryInitializer();
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
        
        ConditionService service = new ConditionServiceImpl();
        Condition toBeSaved = new Condition();
        toBeSaved.setId(0);
        toBeSaved.setGroupId(4);
        toBeSaved.setLastDaysCondition(3);
        toBeSaved.setTargetId(2);
        toBeSaved.setUsageTimeCondition(123456);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(Condition.ID_POSITION)).thenReturn(879L);
        
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(879, retrieved.get(0).getId());
    }
    
}
