package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigElementServiceImplementation implements ConfigElementService {
    
    private static DbCache<String,ConfigElement> dbCache;
    
    public ConfigElementServiceImplementation() {
        setCacheInstance();
    }
    
    private void setCacheInstance() {
        if (dbCache == null) {
            dbCache = ConfigElementFactory.getDbCache();
        }
    }

    @Override
    public Single<Long> add(ConfigElement element) {
        if (isInvalid(element)) {
            return Single.just(SaveAction.ERROR.get());
        }
        return dbCache.save(element).map(SaveAction::get);
    }
    
    private boolean isInvalid(ConfigElement element) {
        return element == null || element.getKey() == null || element.getValue() == null || element.getValue().length() > ConfigElement.MAX_LENGTH;
    }

    @Override
    public Single<Long> update(ConfigElement element) {
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<ConfigElement> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Single<ConfigElement> findById(ConfigurationEnum key) {
        return dbCache.read(key.toString()).defaultIfEmpty(new ConfigElement(key, key.getDefault()));
    }

    @Override
    public Single<Long> deleteById(ConfigurationEnum key) {
        if (key == null) {
            return Single.just(-1L);
        }
        return dbCache.remove(key.toString()).map(b -> b?1L:0L);
    }

    @Override
    public Observable<ConfigElement> allConfigElements() {
        return dbCache.getAll();
    }

    @Override
    public Single<ConfigElement> configElement(ConfigurationEnum key) {
        return findById(key);
    }

}
