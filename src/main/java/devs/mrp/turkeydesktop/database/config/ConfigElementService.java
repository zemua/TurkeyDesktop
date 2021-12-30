/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ConfigElementService implements IConfigElementService {
    
    private final ConfigElementDao repo = ConfigElementRepository.getInstance();
    
    @Override
    public long add(ConfigElement element) {
        if (element == null) {
            return -1;
        } else {
            return repo.add(element);
        }
    }

    @Override
    public long update(ConfigElement element) {
        if (element == null || element.getKey() == null) {
            return -1;
        } else {
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
        return elements;
    }

    @Override
    public ConfigElement findById(String key) {
        ResultSet set = repo.findById(key);
        ConfigElement element = null;
        try {
            if (set.next()) {
                element = elementFromResultSetEntry(set);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return element;
    }

    @Override
    public long deleteById(String key) {
        if (key == null) {
            return -1;
        }
        return repo.deleteById(key);
    }
    
    private ConfigElement elementFromResultSetEntry(ResultSet set) {
        ConfigElement el = new ConfigElement();
        try {
            el.setKey(set.getString(ConfigElement.KEY));
            el.setValue(set.getString(ConfigElement.VALUE));
        } catch (SQLException ex) {
            Logger.getLogger(ConfigElementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
