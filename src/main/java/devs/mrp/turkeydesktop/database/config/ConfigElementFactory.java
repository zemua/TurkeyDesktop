package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementFactory {
    
    private static Supplier<DbCache<String, ConfigElement>> dbCacheSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<String, ConfigElement>> dbCacheSupplier) {
        ConfigElementFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<String, ConfigElement> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static ConfigElementService getService() {
        return new ConfigElementServiceImplementation();
    }
    
    public static void runConditionListWorker(Supplier<List<ConfigElement>> supplier, Consumer<List<ConfigElement>> consumer) {
        new GenericWorker<List<ConfigElement>>().runWorker(supplier, consumer);
    }
    
    public static void runConditionWorker(Supplier<ConfigElement> supplier, Consumer<ConfigElement> consumer) {
        new GenericWorker<ConfigElement>().runWorker(supplier, consumer);
    }
    
    public static Observable<ConfigElement> elementsFromResultSet(ResultSet set) {
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

    public static ConfigElement elementFromResultSetEntry(ResultSet set) {
        ConfigElement el = new ConfigElement();
        try {
            el.setKey(ConfigurationEnum.valueOf(set.getString(ConfigElement.KEY.toString())));
            el.setValue(set.getString(ConfigElement.VALUE));
        } catch (SQLException ex) {
            log.error("Error extracting ConfigElement from ResultSet", ex);
        }
        return el;
    }
    
}
