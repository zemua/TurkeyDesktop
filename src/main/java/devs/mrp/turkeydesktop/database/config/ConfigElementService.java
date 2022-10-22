/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.SingleConsumerFactory;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            findAll(r -> {}); // it assigns values to the hashmap inside the function
        }
    }

    @Override
    public void add(ConfigElement element, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            WorkerFactory.runResultSetWorker(() -> repo.findById(element.getKey().toString()), rs -> {
                try {
                    if (rs.next()) {
                        if (configMap.containsKey(element.getKey()) && configMap.get(element.getKey()) != element.getValue()) {
                            configMap.put(element.getKey(), element.getValue());
                            update(element, consumer);
                        } else {
                            // else the value is the same as the one stored
                            consumer.accept(0);
                        }
                    } else {
                        configMap.put(element.getKey(), element.getValue());
                        WorkerFactory.runLongWorker(() -> repo.add(element), consumer);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public void update(ConfigElement element, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        if (element == null || element.getKey() == null || element.getValue().length() > 150) {
            consumer.accept(-1);
            return;
        }
        configMap.put(element.getKey(), element.getValue());
        WorkerFactory.runLongWorker(() -> repo.update(element), consumer);
    }

    @Override
    public void findAll(Consumer<List<ConfigElement>> c) {
        Consumer<List<ConfigElement>> consumer = new SingleConsumer<>(c);
        List<ConfigElement> elements = new ArrayList<>();
        WorkerFactory.runResultSetWorker(() -> repo.findAll(), set -> {
            try {
                while (set.next()) {
                    ConfigElement el = elementFromResultSetEntry(set);
                    elements.add(el);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            configMap = elements.stream().collect(Collectors.toMap(ConfigElement::getKey, ConfigElement::getValue));
            consumer.accept(elements);
        });
    }
    
    private class ConfigElementWrapper {
        ConfigElement element;
    }

    @Override
    public void findById(ConfigurationEnum key, Consumer<ConfigElement> c) {
        Consumer<ConfigElement> consumer = new SingleConsumer<>(c);
        WorkerFactory.runResultSetWorker(() -> repo.findById(key.toString()), set -> {
            ConfigElementWrapper e = new ConfigElementWrapper();
            try {
                if (set.next()) {
                    e.element = elementFromResultSetEntry(set);
                    configMap.put(e.element.getKey(), e.element.getValue());
                } else {
                    configElement(key, result -> {
                        e.element = result;
                    });
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(e.element);
        });
    }

    @Override
    public void deleteById(ConfigurationEnum key, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        if (key == null) {
            consumer.accept(-1);
            return;
        }
        configMap.remove(key);
        WorkerFactory.runLongWorker(() -> repo.deleteById(key.toString()), consumer);
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
    public void allConfigElements(Consumer<List<ConfigElement>> c) {
        Consumer<List<ConfigElement>> consumer = new SingleConsumer<>(c);
        var result = configMap.entrySet().stream()
                .map(e -> {
                    ConfigElement el = new ConfigElement();
                    el.setKey(e.getKey());
                    el.setValue(e.getValue());
                    return el;
                })
                .collect(Collectors.toList());
        consumer.accept(result);
    }

    @Override
    public void configElement(ConfigurationEnum key, Consumer<ConfigElement> c) {
        Consumer<ConfigElement> consumer = new SingleConsumer<>(c);
        ConfigElement el = new ConfigElement();
        el.setKey(key);
        if (configMap.containsKey(key)) {
            el.setValue(configMap.get(key));
        } else {
            el.setValue(key.getDefault());
        }
        consumer.accept(el);
    }

}
