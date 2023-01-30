package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface ConfigElementService {
    
    public Single<Long> add(ConfigElement element);
    public Single<Long> update(ConfigElement element);
    public Observable<ConfigElement> findAll();
    public Single<ConfigElement> findById(ConfigurationEnum key);
    public Single<Long> deleteById(ConfigurationEnum key);
    
    public Observable<ConfigElement> allConfigElements();
    public Single<ConfigElement> configElement(ConfigurationEnum key);
    
}
