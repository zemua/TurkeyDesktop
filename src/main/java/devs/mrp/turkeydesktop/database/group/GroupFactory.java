package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.common.SingleConsumer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupFactory {
    
    private static Supplier<DbCache<Long, Group>> dbCacheSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<Long, Group>> dbCacheSupplier) {
        GroupFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<Long, Group> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static GroupService getService() {
        return new GroupServiceImpl();
    }
    
    public static void runGroupWorker(Supplier<Group> supplier, Consumer<Group> consumer) {
        new GenericWorker<Group>().runWorker(supplier, consumer);
    }
    
    public static void runGroupListWorker(Supplier<List<Group>> supplier, Consumer<List<Group>> consumer) {
        new GenericWorker<List<Group>>().runWorker(supplier, consumer);
    }
    
    public static Consumer<Group> groupConsumer(Consumer<Group> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<Group>> groupListConsumer(Consumer<List<Group>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Observable<Group> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }
    
    private static Group elementFromResultSetEntry(ResultSet set) {
        Group el = new Group();
        try {
            el.setId(set.getLong(Group.ID));
            el.setName(set.getString(Group.NAME));
            el.setType(Group.GroupType.valueOf(set.getString(Group.TYPE)));
            el.setPreventClose(set.getBoolean(Group.PREVENT_CLOSE));
        } catch (SQLException ex) {
            log.error("Error extracting Group from ResultSet", ex);
        }
        return el;
    }
    
}
