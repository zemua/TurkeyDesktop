/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class TitledLogServiceFacadeFactory {
    
    public static TitledLogServiceFacade getService() {
        return new TitledLogServiceFacadeImpl();
    }
    
    public static Consumer<TitledLog> getConsumer(Consumer<TitledLog> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<TitledLog>> getListConsumer(Consumer<List<TitledLog>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
