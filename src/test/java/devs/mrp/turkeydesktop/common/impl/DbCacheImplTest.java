package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.GeneralDao;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.ResultSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DbCacheImplTest {

    GeneralDao<String, Long> repo;
    Function<String, Long> keyExtractor = s -> Long.parseLong(s.substring(0, 1));
    Function<Long ,Boolean> isValidKey = l -> l > 0;
    Function<ResultSet,Observable<String>> listFromResultSet;
    BiFunction<String,Long,String> keySetter;
    
    DbCacheImpl<Long,String> dbCacheImpl;
    
    @Before
    public void setup() throws InterruptedException {
        repo = mock(GeneralDao.class);
        listFromResultSet = mock(Function.class);
        keySetter = (s,l) -> s;
        
        ResultSet findAllSet = mock(ResultSet.class);
        when(repo.findAll()).thenReturn(Single.just(findAllSet));
        when(listFromResultSet.apply(ArgumentMatchers.eq(findAllSet))).thenReturn(Observable.just("1as", "2qw", "3er", "4vb", "5ty"));
        
        dbCacheImpl = new DbCacheImpl(repo, keyExtractor, isValidKey, listFromResultSet, keySetter);
    }
    
    @Test
    public void testSaveInvalidKeyForKeyTransformer() {
        when(repo.add(ArgumentMatchers.any())).thenReturn(Single.just(9L));
        boolean exceptionTriggered=false;
        try {
            dbCacheImpl.save("invalid").blockingGet();
        } catch (NumberFormatException e) {
            exceptionTriggered = true;
        }
        assertTrue(exceptionTriggered);
    }
    
    @Test
    public void testSaveEmptyInvalidKeyForKeyTransformer() {
        when(repo.add(ArgumentMatchers.any())).thenReturn(Single.just(9L));
        boolean exceptionTriggered=false;
        try {
            dbCacheImpl.save("").blockingGet();
        } catch (StringIndexOutOfBoundsException e) {
            exceptionTriggered = true;
        }
        assertTrue(exceptionTriggered);
    }
    
    @Test
    public void testSaveNew() {
        when(repo.add(ArgumentMatchers.matches("6op"))).thenReturn(Single.just(6L));
        SaveAction result = dbCacheImpl.save("6op").blockingGet();
        assertEquals(SaveAction.SAVED, result);
    }
    
    @Test
    public void testUpdateExisting() {
        when(repo.update(ArgumentMatchers.matches("5up"))).thenReturn(Single.just(1L));
        SaveAction result = dbCacheImpl.save("5up").blockingGet();
        assertEquals(SaveAction.UPDATED, result);
    }
    
    @Test
    public void testAlreadyExists() {
        SaveAction result = dbCacheImpl.save("4vb").blockingGet();
        assertEquals(SaveAction.EXISTING, result);
    }
    
    @Test
    public void testDoubleSave() {
        when(repo.add(ArgumentMatchers.matches("7op"))).thenReturn(Single.just(6L));
        SaveAction resultOne = dbCacheImpl.save("7op").blockingGet();
        assertEquals(SaveAction.SAVED, resultOne);
        
        when(repo.add(ArgumentMatchers.matches("8op"))).thenReturn(Single.just(6L));
        SaveAction resultTwo = dbCacheImpl.save("8op").blockingGet();
        assertEquals(SaveAction.SAVED, resultTwo);
        
        verify(repo, times(1)).add(ArgumentMatchers.matches("7op"));
        verify(repo, times(1)).add(ArgumentMatchers.matches("8op"));
    }
    
    @Test
    public void save_new_saves_in_cache_with_repo_generated_id(){
        GeneralDao<TestObject, Long> testRepo = mock(GeneralDao.class);
        Function<TestObject, Long> testKeyExtractor = testObject -> testObject.key;
        Function<Long ,Boolean> testIsValidKey = l -> true;
        Function<ResultSet,Observable<TestObject>> testListFromResultSet = mock(Function.class);
        BiFunction<TestObject, Long, TestObject> testKeySetter = (o,l) -> {
            o.key = l;
            return o;
        };

        ResultSet testFindAllSet = mock(ResultSet.class);
        when(testRepo.findAll()).thenReturn(Single.just(testFindAllSet));
        when(testListFromResultSet.apply(ArgumentMatchers.eq(testFindAllSet))).thenReturn(Observable.just(new TestObject(1L, "hola"), new TestObject(2L, "adios")));
        DbCacheImpl<Long,TestObject> testDbCacheImpl = new DbCacheImpl(testRepo, testKeyExtractor, testIsValidKey, testListFromResultSet, testKeySetter);
        
        TestObject toBeSaved = new TestObject(8L, "salam malicum");
        long generatedId = 3L;
        when(testRepo.add(ArgumentMatchers.refEq(toBeSaved))).thenReturn(Single.just(generatedId));
        
        testDbCacheImpl.save(toBeSaved).blockingGet();
        
        var retrieveResult = testDbCacheImpl.read(3L).blockingGet();
        assertEquals(toBeSaved.content, retrieveResult.content);
        assertEquals(generatedId, retrieveResult.key.longValue());
    }
    
    @AllArgsConstructor
    class TestObject {
        Long key;
        String content;
    }
    
}
