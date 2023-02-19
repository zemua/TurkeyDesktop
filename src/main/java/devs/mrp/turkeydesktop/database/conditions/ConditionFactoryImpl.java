package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionFactoryImpl implements ConditionFactory {
    
    private final FactoryInitializer factory;
    private static DbCache<Long, Condition> dbCache;
    private static ConditionService conditionService;

    public ConditionFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    protected DbCache<Long, Condition> buildCache(ConditionDao repo) {
        return DbCacheFactory.getDbCache(repo,
            c -> c.getId(),
            key -> ConditionValidator.isValidKey(key),
            this::elementsFromResultSet,
            (condition,id) -> {
                condition.setId(id);
                return condition;
            });
    }
    
    @Override
    public DbCache<Long, Condition> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new ConditionRepository(this));
        }
        return dbCache;
    }
    
    @Override
    public ConditionService getService() {
        if (conditionService == null) {
            conditionService = new ConditionServiceImpl(this);
        }
        return conditionService;
    }
    
    @Override
    public Observable<Condition> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while(set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }
    
    private Condition elementFromResultSetEntry(ResultSet set) {
        Condition el = new Condition();
        try {
            el.setId(set.getLong(Condition.ID));
            el.setGroupId(set.getLong(Condition.GROUP_ID));
            el.setTargetId(set.getLong(Condition.TARGET_ID));
            el.setUsageTimeCondition(set.getLong(Condition.USAGE_TIME_CONDITION));
            el.setLastDaysCondition(set.getLong(Condition.LAST_DAYS_CONDITION));
        } catch (SQLException ex) {
            log.error("Error creating Condition from ResultSet", ex);
        }
        return el;
    }

    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }
    
}
