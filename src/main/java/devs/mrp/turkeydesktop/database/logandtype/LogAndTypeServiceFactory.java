/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class LogAndTypeServiceFactory {
    
    public static LogAndTypeFacadeService getService() {
        return new LogAndTypeFacadeServiceImpl();
    }
    
    public static Consumer<List<Tripla<String, Long, Type.Types>>> getTriplaConsumer(Consumer<List<Tripla<String, Long, Type.Types>>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<TimeLog> getConsumer(Consumer<TimeLog> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
