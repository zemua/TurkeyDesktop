/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface IConfigElementService {
    
    public Observable<Long> add(ConfigElement element);
    public Observable<Long> update(ConfigElement element);
    public Observable<List<ConfigElement>> findAll();
    public Observable<ConfigElement> findById(ConfigurationEnum key);
    public Observable<Long> deleteById(ConfigurationEnum key);
    
    public Observable<List<ConfigElement>> allConfigElements();
    public Observable<ConfigElement> configElement(ConfigurationEnum key);
    
}
