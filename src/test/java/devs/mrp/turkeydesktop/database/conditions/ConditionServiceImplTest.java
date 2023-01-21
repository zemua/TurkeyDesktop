package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
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
        DbFactory.setDbSupplier(() -> db);
        ConditionFactory.setDbCacheSupplier(() -> dbCache);
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
    
}
