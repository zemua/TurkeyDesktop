package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.GenericWorker;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigElementFactory {
    
    
    
    public static IConfigElementService getService() {
        return new ConfigElementService();
    }
    
    public static void runConditionListWorker(Supplier<List<ConfigElement>> supplier, Consumer<List<ConfigElement>> consumer) {
        new GenericWorker<List<ConfigElement>>().runWorker(supplier, consumer);
    }
    
    public static void runConditionWorker(Supplier<ConfigElement> supplier, Consumer<ConfigElement> consumer) {
        new GenericWorker<ConfigElement>().runWorker(supplier, consumer);
    }
    
}
