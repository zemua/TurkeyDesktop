package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupAssignationServiceImplTest {
    
    static final Db db = mock(Db.class);
    static final DbCache<GroupAssignationDao.ElementId, GroupAssignation> dbCache = mock(DbCache.class);
    static final GroupAssignationRepository groupAssignationRepository = mock(GroupAssignationRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
        GroupAssignationFactory.setDbCacheSupplier(() -> dbCache);
    }

    @Test
    public void testAddNullAssignation() {
        GroupAssignationService service = new GroupAssignationServiceImpl();
        GroupAssignation assignation = null;
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithNullType() {
        GroupAssignationService service = new GroupAssignationServiceImpl();
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("some title");
        assignation.setGroupId(3);
        assignation.setType(null);
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithInvalidElementName() {
        GroupAssignationService service = new GroupAssignationServiceImpl();
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("");
        assignation.setGroupId(3);
        assignation.setType(GroupAssignation.ElementType.TITLE);
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithInvalidGroupId() {
        GroupAssignationService service = new GroupAssignationServiceImpl();
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("some title");
        assignation.setGroupId(0);
        assignation.setType(GroupAssignation.ElementType.TITLE);
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddSuccess() {
        GroupAssignationService service = new GroupAssignationServiceImpl();
        GroupAssignation assignation = new GroupAssignation();
        assignation.setElementId("some title");
        assignation.setGroupId(3);
        assignation.setType(GroupAssignation.ElementType.TITLE);
        
        when(dbCache.save(assignation)).thenReturn(Single.just(SaveAction.SAVED));
        
        Long addResult = service.add(assignation).blockingGet();
        assertEquals(SaveAction.SAVED.get(), addResult);
    }
    
}
