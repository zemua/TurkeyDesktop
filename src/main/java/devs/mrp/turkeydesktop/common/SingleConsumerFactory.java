package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class SingleConsumerFactory {
    
    private FactoryInitializer factory;
    
    public SingleConsumerFactory(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    public Consumer<Long> getConsumerOfLong(Consumer<Long> consumer){
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
    public LongConsumer getLongConsumer(LongConsumer consumer) {
        return new SingleLongConsumer(consumer, factory.getToaster());
    }
    
    public Consumer<Integer> getConsumerOfInt(Consumer<Integer> consumer){
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
    public IntConsumer getIntConsumer(IntConsumer consumer) {
        return new SingleIntConsumer(consumer, factory.getToaster());
    }
    
    public Consumer<ResultSet> getResulSetConsumer(Consumer<ResultSet> consumer) {
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
    public Consumer<Boolean> getBooleanConsumer(Consumer<Boolean> consumer) {
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
    public Consumer<List<String>> getStringListConsumer(Consumer<List<String>> consumer) {
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
}
