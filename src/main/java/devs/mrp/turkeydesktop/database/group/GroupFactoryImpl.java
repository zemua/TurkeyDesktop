package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupFactoryImpl implements GroupFactory {
    
    private final FactoryInitializer factory;
    private final DbCache<Long, Group> dbCache;
    private final GroupService groupService;
    
    public GroupFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
        this.dbCache = buildCache();
        this.groupService = new GroupServiceImpl(this);
    }
    
    private DbCache<Long, Group> buildCache() {
        return DbCacheFactory.getDbCache(new GroupRepository(this),
            Group::getId,
            key -> GroupValidator.isValidKey(key),
            this::elementsFromResultSet,
            (group,id) -> {
                group.setId(id);
                return group;
            });
    }
    
    @Override
    public DbCache<Long, Group> getDbCache() {
        return dbCache;
    }
    
    @Override
    public GroupService getService() {
        return groupService;
    }
    
    @Override
    public Observable<Group> elementsFromResultSet(ResultSet set) {
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
    
    private Group elementFromResultSetEntry(ResultSet set) {
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

    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }
    
}
