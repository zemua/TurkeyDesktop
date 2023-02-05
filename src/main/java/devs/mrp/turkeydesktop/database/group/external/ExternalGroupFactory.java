package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.DbCache;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalGroupFactory {
    
    private static Supplier<DbCache<Long,ExternalGroup>> dbCacheSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<Long, ExternalGroup>> dbCacheSupplier) {
        ExternalGroupFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<Long,ExternalGroup> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static ExternalGroupService getService() {
        return new ExternalGroupServiceImpl();
    }
    
    public static Observable<ExternalGroup> elementsFromResultSet(ResultSet set) {
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

    private static ExternalGroup elementFromResultSetEntry(ResultSet set) {
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
    
}
