/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.database.logs.TimeLogService;
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

/**
 *
 * @author miguel
 */
public class ConfigElementService implements IConfigElementService {
    
    private final ConfigElementDao repo = ConfigElementRepository.getInstance();
    
    private static Map<ConfigurationEnum,String> configMap;
    
    public ConfigElementService() {
        initConfigMap();
    }
    
    private void initConfigMap() {
        if (configMap == null) {
            configMap = new HashMap<>();
            findAll(); // it assigns values to the hashmap inside the function
        }
    }
    
    @Override
    public long add(ConfigElement element) {
        if (element == null) {
            return -1;
        } else {
            configMap.put(element.getKey(), element.getValue());
            return repo.add(element);
        }
    }

    @Override
    public long update(ConfigElement element) {
        if (element == null || element.getKey() == null) {
            return -1;
        } else {
            configMap.put(element.getKey(), element.getValue());
            return repo.update(element);
        }
    }

    @Override
    public List<ConfigElement> findAll() {
        List<ConfigElement> elements = new ArrayList<>();
        ResultSet set = repo.findAll();
        try {
            while (set.next()) {
                ConfigElement el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigElementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        configMap.clear();
        elements.forEach(e -> configMap.put(e.getKey(), e.getValue()));
        return elements;
    }

    @Override
    public ConfigElement findById(ConfigurationEnum key) {
        ResultSet set = repo.findById(key.toString());
        ConfigElement element = null;
        try {
            if (set.next()) {
                element = elementFromResultSetEntry(set);
            } else {
                element = configElement(key);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        configMap.put(element.getKey(), element.getValue());
        return element;
    }

    @Override
    public long deleteById(ConfigurationEnum key) {
        if (key == null) {
            return -1;
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
            Logger.getLogger(ConfigElementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public List<ConfigElement> allConfigElements() {
        return configMap.entrySet().stream()
                .map(e -> {
                    ConfigElement el = new ConfigElement();
                    el.setKey(e.getKey());
                    el.setValue(e.getValue());
                    return el;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ConfigElement configElement(ConfigurationEnum key) {
        ConfigElement el = new ConfigElement();
        el.setKey(key);
        if (configMap.containsKey(key)) {
            el.setValue(configMap.get(key));
        } else {
            el.setValue(key.getDefault());
        }
        return el;
    }
    
}
