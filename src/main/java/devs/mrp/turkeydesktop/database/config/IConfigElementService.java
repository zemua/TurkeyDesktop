/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface IConfigElementService {
    
    public void add(ConfigElement element, LongConsumer consumer);
    public void update(ConfigElement element, LongConsumer consumer);
    public void findAll(Consumer<List<ConfigElement>> consumer);
    public void findById(ConfigurationEnum key, Consumer<ConfigElement> consumer);
    public void deleteById(ConfigurationEnum key, LongConsumer consumer);
    
    public List<ConfigElement> allConfigElements();
    public ConfigElement configElement(ConfigurationEnum key);
    
}
