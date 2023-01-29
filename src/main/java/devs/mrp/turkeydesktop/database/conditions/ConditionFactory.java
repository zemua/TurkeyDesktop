package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionFactory {
    
    private static Supplier<DbCache<Long, Condition>> dbCacheSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<Long, Condition>> dbCacheSupplier) {
        ConditionFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<Long, Condition> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static ConditionService getService() {
        return new ConditionServiceImpl();
    }
    
    public static Observable<Condition> elementsFromResultSet(ResultSet set) {
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
    
    private static Condition elementFromResultSetEntry(ResultSet set) {
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
    
}
