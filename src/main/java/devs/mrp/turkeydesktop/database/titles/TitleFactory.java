package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleFactory {
    
    private static Supplier<Db> dbSupplier;
    private static Supplier<DbCache<String,Title>> dbCacheSupplier;

    public static void setDbSupplier(Supplier<Db> dbSupplier) {
        TitleFactory.dbSupplier = dbSupplier;
    }
    
    public static Db getDb() {
        return dbSupplier.get();
    }
    
    public static void setDbCacheSupplier(Supplier<DbCache<String, Title>> dbCacheSupplier) {
        TitleFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<String, Title> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static TitleService getService() {
        return new TitleServiceImpl();
    }
    
    public static Observable<Title> elementsFromResultEntry(ResultSet set) {
        return Observable.create(subscribe -> {
            try {
                while (set.next()) {
                    subscribe.onNext(titleFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscribe.onError(ex);
            }
            subscribe.onComplete();
        });
    }
    
    public static Title titleFromResultSetEntry(ResultSet set) {
        Title el = new Title();
        try {
            el.setSubStr(set.getString(Title.SUB_STR).toLowerCase());
            el.setType(Title.Type.valueOf(set.getString(Title.TYPE)));
        } catch (SQLException ex) {
            log.error("Error transforming Title from ResultSet entry", ex);
        }
        return el;
    }
    
}
