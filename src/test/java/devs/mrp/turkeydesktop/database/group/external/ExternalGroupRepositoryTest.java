package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
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

public class ExternalGroupRepositoryTest {
    
    static Db db = CommonMocks.getMock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static ExternalGroupRepository externalGroupRepository;
    
    ExternalGroup externalGroup;
    
    @BeforeClass
    public static void classSetup() {
        DbFactoryImpl.setDbSupplier(() -> db);
        externalGroupRepository = ExternalGroupRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        externalGroup = new ExternalGroup();
        externalGroup.setFile("some/path");
        externalGroup.setGroup(2);
        externalGroup.setId(0);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet expectedResult = mock(ResultSet.class);
        externalGroup.setId(4);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUPS_EXTERNAL_TABLE WHERE ID=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(expectedResult);
        
        ResultSet result = externalGroupRepository.findById(externalGroup.getId()).blockingGet();
        assertEquals(expectedResult, result);
        verify(preparedStatement, times(1)).setLong(1, externalGroup.getId());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedId = mock(ResultSet.class);
        long expectedIdResult = 8L;
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO GROUPS_EXTERNAL_TABLE (GROUP_COLUMN, FILE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedId);
        when(generatedId.next()).thenReturn(Boolean.TRUE);
        when(generatedId.getLong(ExternalGroup.ID_COLUMN)).thenReturn(expectedIdResult);
        
        long idResult = externalGroupRepository.add(externalGroup).blockingGet();
        assertEquals(expectedIdResult, idResult);
        verify(preparedStatement, times(1)).setLong(1, externalGroup.getGroup());
        verify(preparedStatement, times(1)).setString(2, externalGroup.getFile());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO GROUPS_EXTERNAL_TABLE (GROUP_COLUMN, FILE) VALUES (?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        long idResult = externalGroupRepository.add(externalGroup).blockingGet();
        assertEquals(-1L, idResult);
        verify(preparedStatement, times(1)).setLong(1, externalGroup.getGroup());
        verify(preparedStatement, times(1)).setString(2, externalGroup.getFile());
    }
    
}
