/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 *
 * @author ncm55070
 */
public class SingleConsumerFactory {
    
    public static Consumer<Long> getConsumerOfLong(Consumer<Long> consumer){
        return new SingleConsumer<>(consumer);
    }
    
    public static LongConsumer getLongConsumer(LongConsumer consumer) {
        return new SingleLongConsumer(consumer);
    }
    
    public static Consumer<Integer> getConsumerOfInt(Consumer<Integer> consumer){
        return new SingleConsumer<>(consumer);
    }
    
    public static IntConsumer getIntConsumer(IntConsumer consumer) {
        return new SingleIntConsumer(consumer);
    }
    
    public static Consumer<ResultSet> getResulSetConsumer(Consumer<ResultSet> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<Boolean> getBooleanConsumer(Consumer<Boolean> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<String>> getStringListConsumer(Consumer<List<String>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
