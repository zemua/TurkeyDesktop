/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class ConfigElementService implements IConfigElementService {
    
    public static final DbCache<ConfigurationEnum,ConfigElement> dbCache = DbCacheFactory.getDbCache(ConfigElementRepository.getInstance(),
            c -> c.getKey().toString(),
            ConfigElementService::elementsFromResultSet);

    @Override
    public Single<Long> add(ConfigElement element) {
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return dbCache.save(element).map(SaveAction::get);
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
        return dbCache.read(key).defaultIfEmpty(new ConfigElement(key, key.getDefault()));
    }

    @Override
    public Single<Long> deleteById(ConfigurationEnum key) {
        if (key == null) {
            return Single.just(-1L);
        }
        return dbCache.remove(key).map(b -> b?1L:0L);
    }

    private static Observable<ConfigElement> elementsFromResultSet(ResultSet set) {
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

    private static ConfigElement elementFromResultSetEntry(ResultSet set) {
        ConfigElement el = new ConfigElement();
        try {
            el.setKey(ConfigurationEnum.valueOf(set.getString(ConfigElement.KEY.toString())));
            el.setValue(set.getString(ConfigElement.VALUE));
        } catch (SQLException ex) {
            log.error("Error extracting ConfigElement from ResultSet", ex);
        }
        return el;
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
