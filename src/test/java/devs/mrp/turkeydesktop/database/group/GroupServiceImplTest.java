package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.impl.CommonMocks;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import io.reactivex.rxjava3.core.Single;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupServiceImplTest {
    
    static final Db db = CommonMocks.getMock(Db.class);
    static final DbCache<Long, Group> dbCache = mock(DbCache.class);
    static final GroupRepository groupRepository = mock(GroupRepository.class);
    
    @BeforeClass
    public static void setupClass() {
        DbFactory.setDbSupplier(() -> db);
        GroupFactory.setDbCacheSupplier(() -> dbCache);
    }

    @Test
    public void testAddNullGroup() {
        GroupService service = new GroupServiceImpl();
        Group group = null;
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithBlankName() {
        GroupService service = new GroupServiceImpl();
        Group group = new Group();
        group.setName("");
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithNullName() {
        GroupService service = new GroupServiceImpl();
        Group group = new Group();
        group.setName(null);
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddWithNullType() {
        GroupService service = new GroupServiceImpl();
        Group group = new Group();
        group.setName("some name");
        group.setPreventClose(true);
        group.setType(null);
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.ERROR.get(), addResult);
    }
    
    @Test
    public void testAddSuccess() {
        GroupService service = new GroupServiceImpl();
        Group group = new Group();
        group.setName("some name");
        group.setPreventClose(true);
        group.setType(Group.GroupType.NEGATIVE);
        
        when(dbCache.save(group)).thenReturn(Single.just(SaveAction.SAVED));
        
        Long addResult = service.add(group).blockingGet();
        assertEquals(SaveAction.SAVED.get(), addResult);
    }
    
}
