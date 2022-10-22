/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class TypeServiceFactory {
    
    public static TypeService getService() {
        return new TypeServiceImpl();
    }
    
    public static Consumer<Type> getConsumer(Consumer<Type> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<Type>> getListConsumer(Consumer<List<Type>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
