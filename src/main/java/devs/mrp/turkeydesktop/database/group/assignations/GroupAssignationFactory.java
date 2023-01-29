package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupAssignationFactory {
    
    private static Supplier<GroupAssignationService> groupAssignationServiceSupplier;
    private static Supplier<DbCache<GroupAssignationDao.ElementId, GroupAssignation>> dbCacheSupplier;

    public static void setGroupAssignationServiceSupplier(Supplier<GroupAssignationService> groupAssignationServiceSupplier) {
        GroupAssignationFactory.groupAssignationServiceSupplier = groupAssignationServiceSupplier;
    }
    
    public static GroupAssignationService getService() {
        return groupAssignationServiceSupplier.get();
    }

    public static void setDbCacheSupplier(Supplier<DbCache<GroupAssignationDao.ElementId, GroupAssignation>> dbCacheSupplier) {
        GroupAssignationFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<GroupAssignationDao.ElementId, GroupAssignation> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static Observable<GroupAssignation> elementsFromResultSet(ResultSet set) {
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
    
    private static GroupAssignation elementFromResultSetEntry(ResultSet set) {
        GroupAssignation.GroupAssignationBuilder el = GroupAssignation.builder();
        try {
            el.type(set.getString(GroupAssignation.TYPE) != null ? GroupAssignation.ElementType.valueOf(set.getString(GroupAssignation.TYPE)) : null);
            el.elementId(set.getString(GroupAssignation.ELEMENT_ID));
            el.groupId(set.getLong(GroupAssignation.GROUP_ID));
        } catch (SQLException ex) {
            log.error("Error extracting group assignation from result set", ex);
        }
        return el.build();
    }
    
}
