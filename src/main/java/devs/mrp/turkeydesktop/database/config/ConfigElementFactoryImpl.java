package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementFactoryImpl implements ConfigElementFactory {
    
    @Getter
    private final Db db;
    private static DbCache<String, ConfigElement> dbCache;
    private static ConfigElementService configElementService;
    
    public ConfigElementFactoryImpl(FactoryInitializer factory) {
        db = factory.getDbFactory().getDb();
    }
    
    protected DbCache<String, ConfigElement> buildCache(ConfigElementDao repo) {
        Function<ConfigElement,String> keyExtractor = c -> c.getKey().toString();
        Function<String,Boolean> isNewKey = key -> ConfigElementValidator.isValidKey(key);
        Function<ResultSet,Observable<ConfigElement>> listFromResultSet = this::elementsFromResultSet;
        BiFunction<ConfigElement, String, ConfigElement> keySetter = (element,key) -> element;
        return DbCacheFactory.getDbCache(repo, keyExtractor, isNewKey, listFromResultSet, keySetter);
    }
    
    @Override
    public DbCache<String, ConfigElement> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new ConfigElementRepository(this));
        }
        return dbCache;
    }
    
    @Override
    public ConfigElementService getService() {
        if (configElementService == null) {
            configElementService = new ConfigElementServiceImpl(this);
        }
        return configElementService;
    }
    
    @Override
    public void runConditionListWorker(Supplier<List<ConfigElement>> supplier, Consumer<List<ConfigElement>> consumer) {
        new GenericWorker<List<ConfigElement>>().runWorker(supplier, consumer);
    }
    
    @Override
    public void runConditionWorker(Supplier<ConfigElement> supplier, Consumer<ConfigElement> consumer) {
        new GenericWorker<ConfigElement>().runWorker(supplier, consumer);
    }
    
    @Override
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

    @Override
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
