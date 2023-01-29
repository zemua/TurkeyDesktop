package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupFactory {
    
    private static Supplier<DbCache<Long, Group>> dbCacheSupplier;
    
    private static Supplier<GroupService> groupServiceSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<Long, Group>> dbCacheSupplier) {
        GroupFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<Long, Group> getDbCache() {
        return dbCacheSupplier.get();
    }

    public static void setGroupServiceSupplier(Supplier<GroupService> groupServiceSupplier) {
        GroupFactory.groupServiceSupplier = groupServiceSupplier;
    }
    
    public static GroupService getService() {
        GroupService result;
        if (groupServiceSupplier == null) {
            result = new GroupServiceImpl();
        } else {
            result = groupServiceSupplier.get();
        }
        return result;
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
