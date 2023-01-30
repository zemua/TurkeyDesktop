package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupAssignationServiceImplTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
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
    
    @Test
    public void test_add_sets_object_id_in_cache() throws SQLException {
        FactoryInitializer factoryInitializer = new FactoryInitializer();
        factoryInitializer.setDbSupplier(() -> db);
        factoryInitializer.initialize();
        
        GroupAssignationService service = new GroupAssignationServiceImpl();
        GroupAssignation toBeSaved = new GroupAssignation();
        toBeSaved.setElementId("process or title");
        toBeSaved.setGroupId(4);
        toBeSaved.setType(GroupAssignation.ElementType.TITLE);
        
        PreparedStatement statement = mock(PreparedStatement.class);
        when(db.prepareStatementWithGeneratedKeys(ArgumentMatchers.any())).thenReturn(statement);
        when(db.prepareStatement(ArgumentMatchers.any())).thenReturn(statement);
        
        service.add(toBeSaved).blockingGet();
        
        var retrieved = service.findAll().toList().blockingGet();
        
        assertEquals(toBeSaved.getType(), retrieved.get(0).getType());
        assertEquals(toBeSaved.getElementId(), retrieved.get(0).getElementId());
        assertEquals(toBeSaved.getGroupId(), retrieved.get(0).getGroupId());
    }
    
}
