/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.List;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface IConfigElementService {
    
    public void add(ConfigElement element, LongConsumer consumer);
    public long update(ConfigElement element);
    public List<ConfigElement> findAll();
    public ConfigElement findById(ConfigurationEnum key);
    public long deleteById(ConfigurationEnum key);
    
    public List<ConfigElement> allConfigElements();
    public ConfigElement configElement(ConfigurationEnum key);
    
}
