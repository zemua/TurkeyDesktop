package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConditionRepositoryTest {
    
    static Db db = mock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static ConditionRepository conditionRepository;
    
    Condition condition;
    
    @BeforeClass
    public static void classSetup() {
        DbFactory.setDbSupplier(() -> db);
        conditionRepository = ConditionRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        condition = new Condition();
        condition.setId(0);
        condition.setGroupId(2);
        condition.setLastDaysCondition(1);
        condition.setTargetId(4);
        condition.setUsageTimeCondition(30000);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet conditionResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.refEq("SELECT * FROM CONDITIONS_TABLE WHERE ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(conditionResult);
        
        ResultSet result = conditionRepository.findById(condition.getId()).blockingGet();
        assertEquals(conditionResult, result);
        verify(preparedStatement, times(1)).setLong(1, condition.getId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.refEq("INSERT INTO CONDITIONS_TABLE (GROUP_ID, TARGET_ID, USAGE_TIME_CONDITION, LAST_DAYS_CONDITION) VALUES (?, ?, ?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(1)).thenReturn(3L);
        
        long longResult = conditionRepository.add(condition).blockingGet();
        assertEquals(3L, longResult);
        verify(preparedStatement, times(1)).setLong(1, condition.getGroupId());
        verify(preparedStatement, times(1)).setLong(2, condition.getTargetId());
        verify(preparedStatement, times(1)).setLong(3, condition.getUsageTimeCondition());
        verify(preparedStatement, times(1)).setLong(4, condition.getLastDaysCondition());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.refEq("INSERT INTO CONDITIONS_TABLE (GROUP_ID, TARGET_ID, USAGE_TIME_CONDITION, LAST_DAYS_CONDITION) VALUES (?, ?, ?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(1)).thenReturn(3L);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        long longResult = conditionRepository.add(condition).blockingGet();
        assertEquals(-1L, longResult);
        verify(preparedStatement, times(1)).setLong(1, condition.getGroupId());
        verify(preparedStatement, times(1)).setLong(2, condition.getTargetId());
        verify(preparedStatement, times(1)).setLong(3, condition.getUsageTimeCondition());
        verify(preparedStatement, times(1)).setLong(4, condition.getLastDaysCondition());
    }
    
}
