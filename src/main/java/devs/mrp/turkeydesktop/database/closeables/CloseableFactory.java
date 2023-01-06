package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class CloseableFactory {
    
    private static Supplier<DbCache<String, Closeable>> dbCacheSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<String, Closeable>> dbCacheSupplier) {
        CloseableFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<String, Closeable> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static CloseableService getService() {
        return new CloseableServiceImpl();
    }
    
    public static Observable<Closeable> listFromResultSet(ResultSet set) {
        return Observable.create(suscriber -> {
            try {
                while(set.next()) {
                    suscriber.onNext(new Closeable(set.getString(Closeable.PROCESS_NAME)));
                }
            } catch (SQLException ex) {
                suscriber.onError(ex);
            }
            suscriber.onComplete();
        });
    }
    
}
