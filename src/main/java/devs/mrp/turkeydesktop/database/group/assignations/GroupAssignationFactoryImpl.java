package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupAssignationFactoryImpl implements GroupAssignationFactory {
    
    private final FactoryInitializer factory;
    private static DbCache<GroupAssignationDao.ElementId, GroupAssignation> dbCache;
    private static GroupAssignationService groupAssignationService;
    
    public GroupAssignationFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    private DbCache<GroupAssignationDao.ElementId, GroupAssignation> buildCache() {
        return DbCacheFactory.getDbCache(new GroupAssignationRepository(this),
            element -> new GroupAssignationDao.ElementId(element.getType(), element.getElementId()),
            GroupAssignationValidator::isValidKey,
            this::elementsFromResultSet,
            (assignation,id) -> assignation);
    }
    
    @Override
    public GroupAssignationService getService() {
        if (groupAssignationService == null) {
            groupAssignationService = new GroupAssignationServiceImpl(this);
        }
        return groupAssignationService;
    }
    
    @Override
    public DbCache<GroupAssignationDao.ElementId, GroupAssignation> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache();
        }
        return dbCache;
    }
    
    @Override
    public Observable<GroupAssignation> elementsFromResultSet(ResultSet set) {
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
    
    private GroupAssignation elementFromResultSetEntry(ResultSet set) {
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
