package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalGroupFactoryImpl implements ExternalGroupFactory {
    
    private final FactoryInitializer factory;
    
    public ExternalGroupFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public DbCache<Long,ExternalGroup> getDbCache() {
        return DbCacheFactory.getDbCache(ExternalGroupRepository.getInstance(this),
            ExternalGroup::getId,
            ExternalGroupValidator::isValidKey,
            this::elementsFromResultSet,
            (external,id) -> {
                external.setId(id);
                return external;
            });
    }
    
    @Override
    public ExternalGroupService getService() {
        return ExternalGroupServiceImpl.getInstance(this);
    }
    
    @Override
    public Observable<ExternalGroup> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                log.debug("error observing elementFromResultSet", ex);
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }

    private ExternalGroup elementFromResultSetEntry(ResultSet set) {
        ExternalGroup el = new ExternalGroup();
        try {
            el.setId(set.getLong(ExternalGroup.ID));
            el.setGroup(set.getLong(ExternalGroup.GROUP));
            el.setFile(set.getString(ExternalGroup.FILE));
        } catch (SQLException ex) {
            log.error("Error mapping element from result set", ex);
        }
        return el;
    }

    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }
    
}