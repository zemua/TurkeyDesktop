package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class SingleConsumerFactory {
    
    private static SingleConsumerFactory instance;
    
    private SingleConsumerFactory() {}
    
    public static SingleConsumerFactory getInstance() {
        if (instance == null) {
            instance = new SingleConsumerFactory();
        }
        return instance;
    }
    
    public Consumer<Long> getConsumerOfLong(Consumer<Long> consumer){
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
    public LongConsumer getLongConsumer(LongConsumer consumer) {
        return new SingleLongConsumer(consumer, CommonBeans.getToaster());
    }
    
    public Consumer<Integer> getConsumerOfInt(Consumer<Integer> consumer){
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
    public IntConsumer getIntConsumer(IntConsumer consumer) {
        return new SingleIntConsumer(consumer, CommonBeans.getToaster());
    }
    
    public Consumer<ResultSet> getResulSetConsumer(Consumer<ResultSet> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
    public Consumer<Boolean> getBooleanConsumer(Consumer<Boolean> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
    public Consumer<List<String>> getStringListConsumer(Consumer<List<String>> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
}
