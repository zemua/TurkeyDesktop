/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class ConfigElementService implements IConfigElementService {

    private static final ConfigElementDao repo = ConfigElementRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ConfigElementService.class.getName());

    private static Map<ConfigurationEnum, String> configMap = new HashMap<>();

    public ConfigElementService() {
        initConfigMap();
    }

    private void initConfigMap() {
        if (configMap == null) {
            configMap = new HashMap<>();
        }
        if (configMap.isEmpty()) {
            // assigns values to the hashmap inside the function
            findAll().subscribe();
        }
    }

    @Override
    public Single<Long> add(ConfigElement element) {
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return repo.findById(element.getKey().toString()).flatMap(rs -> {
                try {
                    if (rs.next()) {
                        if (configMap.containsKey(element.getKey()) && configMap.get(element.getKey()) != element.getValue()) {
                            configMap.put(element.getKey(), element.getValue());
                            return update(element);
                        }
                    } else {
                        configMap.put(element.getKey(), element.getValue());
                        return repo.add(element);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ConfigElementService.class.getName()).log(Level.SEVERE, null, ex);
                }
                return Single.just(0L);
        });
    }

    @Override
    public Single<Long> update(ConfigElement element) {
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            return Single.just(-1L);
        }
        configMap.put(element.getKey(), element.getValue());
        return repo.update(element);
    }

    @Override
    public Observable<ConfigElement> findAll() {
        return repo.findAll().flatMapObservable(set -> {
            return Observable.create(subscriber -> {
                configMap.clear();
                try {
                    while (set.next()) {
                        ConfigElement elem = elementFromResultSetEntry(set);
                        configMap.put(elem.getKey(), elem.getValue());
                        subscriber.onNext(elem);
                    }
                } catch (SQLException ex) {
                    subscriber.onError(ex);
                }
                subscriber.onComplete();
            });
        });
    }
    
    private class ConfigElementWrapper {
        ConfigElement element;
    }

    @Override
    public Single<ConfigElement> findById(ConfigurationEnum key) {
        return repo.findById(key.toString()).flatMap(set -> {
            ConfigElementWrapper e = new ConfigElementWrapper();
            try {
                if (set.next()) {
                    e.element = elementFromResultSetEntry(set);
                    configMap.put(e.element.getKey(), e.element.getValue());
                    return Single.just(e.element);
                }
                return configElement(key);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return Single.just(e.element);
        });
    }

    @Override
    public Single<Long> deleteById(ConfigurationEnum key) {
        if (key == null) {
            return Single.just(-1L);
        }
        configMap.remove(key);
        return repo.deleteById(key.toString());
    }

    private ConfigElement elementFromResultSetEntry(ResultSet set) {
        ConfigElement el = new ConfigElement();
        try {
            el.setKey(ConfigurationEnum.valueOf(set.getString(ConfigElement.KEY.toString())));
            el.setValue(set.getString(ConfigElement.VALUE));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public Observable<ConfigElement> allConfigElements() {
        return Observable.fromIterable(configMap.entrySet())
                .map(e -> {
                    ConfigElement el = new ConfigElement();
                    el.setKey(e.getKey());
                    el.setValue(e.getValue());
                    return el;
                });
    }

    @Override
    public Single<ConfigElement> configElement(ConfigurationEnum key) {
        ConfigElement el = new ConfigElement();
        el.setKey(key);
        if (configMap.containsKey(key)) {
            el.setValue(configMap.get(key));
        } else {
            el.setValue(key.getDefault());
        }
        return Single.just(el);
    }

}
