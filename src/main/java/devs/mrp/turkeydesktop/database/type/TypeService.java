/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface TypeService {
    
    public void add(Type element, LongConsumer consumer);
    public void update(Type element, LongConsumer consumer);
    public Observable<List<Type>> findAll();
    public void findById(String id, Consumer<Type> consumer);
    public void deleteById(String id, LongConsumer consumer);
    public void findByType(Type.Types type, Consumer<List<Type>> consumer);
    
}
