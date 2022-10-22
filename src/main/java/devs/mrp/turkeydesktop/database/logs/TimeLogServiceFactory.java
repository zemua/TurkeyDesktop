/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class TimeLogServiceFactory {
    
    public static TimeLogService getService() {
        return new TimeLogServiceImpl();
    }
    
    public static Consumer<List<TimeLog>> getListConsumer(Consumer<List<TimeLog>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<Dupla<String,Long>>> getListDuplaConsumer(Consumer<List<Dupla<String,Long>>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<TimeLog> getConsumer(Consumer<TimeLog> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
