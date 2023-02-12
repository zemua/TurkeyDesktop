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
    
    private FactoryInitializer factory;
    
    public GroupAssignationFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public GroupAssignationService getService() {
        return GroupAssignationServiceImpl.getInstance(this);
    }
    
    @Override
    public DbCache<GroupAssignationDao.ElementId, GroupAssignation> getDbCache() {
        return DbCacheFactory.getDbCache(GroupAssignationRepository.getInstance(this),
            element -> new GroupAssignationDao.ElementId(element.getType(), element.getElementId()),
            GroupAssignationValidator::isValidKey,
            this::elementsFromResultSet,
            (assignation,id) -> assignation);
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
