package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementFactoryImpl implements ConfigElementFactory {
    
    private Supplier<DbCache<String, ConfigElement>> dbCacheSupplier;
    
    private Supplier<Db> dbSupplier;
    
    private Supplier<ConfigElementDao> repoSupplier;

    public void setDbCacheSupplier(Supplier<DbCache<String, ConfigElement>> dbCacheSupplier) {
        this.dbCacheSupplier = dbCacheSupplier;
    }
    
    public DbCache<String, ConfigElement> getDbCache() {
        return dbCacheSupplier.get();
    }

    public void setDbSupplier(Supplier<Db> dbSupplier) {
        this.dbSupplier = dbSupplier;
    }
    
    public Db getDb() {
        Db result;
        if (dbSupplier == null) {
            result = DbFactory.getDb();
        } else {
            result = dbSupplier.get();
        }
        return result;
    }

    public void setRepoSupplier(Supplier<ConfigElementDao> repoSupplier) {
        this.repoSupplier = repoSupplier;
    }
    
    public ConfigElementDao getRepo() {
        ConfigElementDao result;
        if (repoSupplier == null) {
            result = ConfigElementRepository.getInstance(this);
        } else {
            result = repoSupplier.get();
        }
        return result;
    }
    
    public ConfigElementService getService() {
        return new ConfigElementServiceImpl(this);
    }
    
    public void runConditionListWorker(Supplier<List<ConfigElement>> supplier, Consumer<List<ConfigElement>> consumer) {
        new GenericWorker<List<ConfigElement>>().runWorker(supplier, consumer);
    }
    
    public void runConditionWorker(Supplier<ConfigElement> supplier, Consumer<ConfigElement> consumer) {
        new GenericWorker<ConfigElement>().runWorker(supplier, consumer);
    }
    
    public Observable<ConfigElement> elementsFromResultSet(ResultSet set) {
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

    public ConfigElement elementFromResultSetEntry(ResultSet set) {
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
