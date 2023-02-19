package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class TypeRepositoryTest {
    
    Db db = mock(Db.class);
    PreparedStatement allPreparedStatement;
    ResultSet allResultSet = mock(ResultSet.class);
    TypeFactory factory = mock(TypeFactory.class);
    
    TypeRepository typeRepository;
    
    Type type;
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        typeRepository = new TypeRepository(factory);
    }
    
    @Before
    public void setup() throws SQLException {
        allPreparedStatement = mock(PreparedStatement.class);
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        type = new Type();
        type.setProcess("some process");
        type.setType(Type.Types.DEPENDS);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet expectedResult = mock(ResultSet.class);
        
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM TYPES_CATEGORIZATION WHERE PROCESS_NAME=?")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(expectedResult);
        
        typeRepository.findById(type.getProcess());
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet result = captor.getValue().call();
        
        assertEquals(expectedResult, result);
        verify(preparedStatement, times(1)).setString(1, type.getProcess());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        String expectedResult = type.getProcess();
        
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO TYPES_CATEGORIZATION (PROCESS_NAME, TYPE) VALUES (?,?)")))
                .thenReturn(preparedStatement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(preparedStatement);
        
        typeRepository.add(type);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String idResult = captor.getValue().call();
        
        assertEquals(expectedResult, idResult);
        verify(preparedStatement, times(1)).setString(1, type.getProcess());
        verify(preparedStatement, times(1)).setString(2, type.getType().toString());
    }
    
    @Test
    public void testAddException() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        String expectedResult = "";
        
        when(db.prepareStatement(ArgumentMatchers.contains("INSERT INTO TYPES_CATEGORIZATION (PROCESS_NAME, TYPE) VALUES (?,?)")))
                .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        typeRepository.add(type);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String idResult = captor.getValue().call();
        
        assertEquals(expectedResult, idResult);
        verify(preparedStatement, times(1)).setString(1, type.getProcess());
        verify(preparedStatement, times(1)).setString(2, type.getType().toString());
    }

}
