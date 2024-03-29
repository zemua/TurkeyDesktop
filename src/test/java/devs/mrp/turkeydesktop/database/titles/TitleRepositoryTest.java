package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.database.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TitleRepositoryTest {
    
    Db db = mock(Db.class);
    Connection connection = mock(Connection.class);
    PreparedStatement allPreparedStatement = mock(PreparedStatement.class);
    ResultSet allResultSet = mock(ResultSet.class);
    TitleFactory factory = mock(TitleFactory.class);
    
    TitleRepository titleRepository;
    
    Title title;
    
    @Before
    public void classSetup() {
        when(factory.getDb()).thenReturn(db);
        titleRepository = new TitleRepository(factory);
    }
    
    @Before
    public void setup() throws SQLException {
        when(db.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(ArgumentMatchers.matches("SELECT * FROM TITLES_TABLE"))).thenReturn(allPreparedStatement);
        when(allPreparedStatement.executeQuery()).thenReturn(allResultSet);
        
        title = new Title();
        title.setSubStr("my test title");
        title.setType(Title.Type.POSITIVE);
    }

    @Test
    public void testFindByIdReturnsResultSet() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatement(ArgumentMatchers.contains("SELECT * FROM TITLES_TABLE WHERE SUB_STR=?"))).thenReturn(preparedStatement);
        ResultSet titleResult = mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(titleResult);
        
        titleRepository.findById(title.getSubStr());
        ArgumentCaptor<Callable<ResultSet>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleResultSet(captor.capture());
        ResultSet resultset = captor.getValue().call();
        
        assertEquals(titleResult, resultset);
        verify(preparedStatement, times(1)).setString(1, title.getSubStr());
    }
    
    @Test
    public void testAddSuccess() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO TITLES_TABLE (SUB_STR, TYPE) VALUES (?,?)"))).thenReturn(preparedStatement);
        
        titleRepository.add(title);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String result = captor.getValue().call();
        
        assertEquals(title.getSubStr(), result);
        verify(preparedStatement, times(1)).setString(1, title.getSubStr());
        verify(preparedStatement, times(1)).setString(2, title.getType().toString());
    }
    
    @Test
    public void testAddFails() throws SQLException, Exception {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.contains("INSERT INTO TITLES_TABLE (SUB_STR, TYPE) VALUES (?,?)"))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
        
        titleRepository.add(title);
        ArgumentCaptor<Callable<String>> captor = ArgumentCaptor.forClass(Callable.class);
        verify(db).singleString(captor.capture());
        String result = captor.getValue().call();
        
        assertEquals("", result);
        verify(preparedStatement, times(1)).setString(1, title.getSubStr());
        verify(preparedStatement, times(1)).setString(2, title.getType().toString());
    }

}
