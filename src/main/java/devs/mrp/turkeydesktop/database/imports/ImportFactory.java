package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ImportFactory {
    
    private static Supplier<DbCache<String, String>> dbCacheSupplier;
    
    public static Db getDb() {
        return DbFactoryImpl.getDb();
    }
    
    public static DbCache<String, String> getDbCache() {
        return dbCacheSupplier.get();
    }

    public static void setDbCacheSupplier(Supplier<DbCache<String, String>> dbCacheSupplier) {
        ImportFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static ImportService getService() {
        return new ImportServiceImpl();
    }
    
    public static Observable<String> elementsFromSet(ResultSet set) {
        return Observable.create(subscribe -> {
            try {
                while (set.next()) {
                    subscribe.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscribe.onError(ex);
            }
            subscribe.onComplete();
        });
    }
    
    public static String elementFromResultSetEntry(ResultSet set) {
        try {
            return set.getString(ConfigurationEnum.IMPORT_PATH.toString());
        } catch (SQLException ex) {
            log.error("Error extracting Import from ResultSet", ex);
        }
        return StringUtils.EMPTY;
    }
}
