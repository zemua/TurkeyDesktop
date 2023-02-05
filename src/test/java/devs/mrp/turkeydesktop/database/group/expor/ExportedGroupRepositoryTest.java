package devs.mrp.turkeydesktop.database.group.expor;

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

public class ExportedGroupRepositoryTest {
    
    static Db db = CommonMocks.getMock(Db.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    
    static ExportedGroupRepository exportedGroupRepository;
    
    ExportedGroup exportedGroup;
    
    @BeforeClass
    public static void classSetup() {
        DbFactoryImpl.setDbSupplier(() -> db);
        exportedGroupRepository = ExportedGroupRepository.getInstance();
    }
    
    @Before
    public void setup() throws SQLException {
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        exportedGroup = new ExportedGroup();
        exportedGroup.setDays(3);
        exportedGroup.setFile("some/file/path");
        exportedGroup.setGroup(7);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet expectedResult = mock(ResultSet.class);
        ExportedGroupId id = new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile());
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM GROUPS_EXPORT_TABLE WHERE GROUP_COLUMN=? AND FILE=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(expectedResult);
        
        ResultSet result = exportedGroupRepository.findById(id).blockingGet();
        assertEquals(expectedResult, result);
        verify(preparedStatement, times(1)).setLong(1, id.getGroup());
        verify(preparedStatement, times(1)).setString(2, id.getFile());
    }
    
    @Test
    public void testAddSuccess() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ExportedGroupId expectedResult = new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile());
        
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO GROUPS_EXPORT_TABLE (GROUP_COLUMN, FILE, FOR_DAYS) VALUES (?, ?, ?)")))
                .thenReturn(preparedStatement);
        
        ExportedGroupId exportedIdResult = exportedGroupRepository.add(exportedGroup).blockingGet();
        assertEquals(expectedResult, exportedIdResult);
        verify(preparedStatement, times(1)).setLong(1, exportedGroup.getGroup());
        verify(preparedStatement, times(1)).setString(2, exportedGroup.getFile());
        verify(preparedStatement, times(1)).setLong(3, exportedGroup.getDays());
    }
    
    @Test
    public void testAddException() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ExportedGroupId expectedResult = new ExportedGroupId(-1, "");
        
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO GROUPS_EXPORT_TABLE (GROUP_COLUMN, FILE, FOR_DAYS) VALUES (?, ?, ?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        ExportedGroupId exportedIdResult = exportedGroupRepository.add(exportedGroup).blockingGet();
        assertEquals(expectedResult, exportedIdResult);
        verify(preparedStatement, times(1)).setLong(1, exportedGroup.getGroup());
        verify(preparedStatement, times(1)).setString(2, exportedGroup.getFile());
        verify(preparedStatement, times(1)).setLong(3, exportedGroup.getDays());
    }
    
}
