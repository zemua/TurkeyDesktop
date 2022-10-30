/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class ConfigElementService implements IConfigElementService {

    private static final ConfigElementDao repo = ConfigElementRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ConfigElementService.class.getName());

    private static Map<ConfigurationEnum, String> configMap;

    public ConfigElementService() {
        initConfigMap();
    }

    private void initConfigMap() {
        if (configMap == null) {
            configMap = new HashMap<>();
            // assigns values to the hashmap inside the function
            findAll().subscribe();
        }
    }

    @Override
    public Observable<Long> add(ConfigElement element) {
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            return Observable.just(-1L);
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
                return Observable.just(0L);
        });
    }

    @Override
    public Observable<Long> update(ConfigElement element) {
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            return Observable.just(-1L);
        }
        configMap.put(element.getKey(), element.getValue());
        return repo.update(element);
    }

    @Override
    public Observable<List<ConfigElement>> findAll() {
        List<ConfigElement> elements = new ArrayList<>();
        return repo.findAll().map(set -> {
            try {
                while (set.next()) {
                    ConfigElement el = elementFromResultSetEntry(set);
                    elements.add(el);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            configMap = elements.stream().collect(Collectors.toMap(ConfigElement::getKey, ConfigElement::getValue));
            return elements;
        });
    }
    
    private class ConfigElementWrapper {
        ConfigElement element;
    }

    @Override
    public Observable<ConfigElement> findById(ConfigurationEnum key) {
        return repo.findById(key.toString()).flatMap(set -> {
            ConfigElementWrapper e = new ConfigElementWrapper();
            try {
                if (set.next()) {
                    e.element = elementFromResultSetEntry(set);
                    configMap.put(e.element.getKey(), e.element.getValue());
                    return Observable.just(e.element);
                }
                return configElement(key);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return Observable.just(e.element);
        });
    }

    @Override
    public Observable<Long> deleteById(ConfigurationEnum key) {
        if (key == null) {
            return Observable.just(-1L);
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
    public Observable<List<ConfigElement>> allConfigElements() {
        return Observable.just(configMap.entrySet().stream()
                .map(e -> {
                    ConfigElement el = new ConfigElement();
                    el.setKey(e.getKey());
                    el.setValue(e.getValue());
                    return el;
                })
                .collect(Collectors.toList()));
    }

    @Override
    public Observable<ConfigElement> configElement(ConfigurationEnum key) {
        ConfigElement el = new ConfigElement();
        el.setKey(key);
        if (configMap.containsKey(key)) {
            el.setValue(configMap.get(key));
        } else {
            el.setValue(key.getDefault());
        }
        return Observable.just(el);
    }

}
