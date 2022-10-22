/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface CloseableService {
    
    public void add(String element, LongConsumer consumer);
    public void findAll(Consumer<List<Closeable>> consumer);
    public void findById(String id, Consumer<Closeable> consumer);
    public void canBeClosed(String process, Consumer<Boolean> consumer);
    public void deleteById(String id, LongConsumer consumer);
    
}
