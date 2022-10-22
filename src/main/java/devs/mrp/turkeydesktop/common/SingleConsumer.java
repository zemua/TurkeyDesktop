/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.util.function.Consumer;

/**
 *
 * @author ncm55070
 */
public class SingleConsumer<T> implements Consumer<T> {
    
    private boolean consumed = false;
    private final Consumer<T> mConsumer;
    
    public SingleConsumer(Consumer<T> consumer) {
        mConsumer = consumer;
    }

    @Override
    public void accept(T t) {
        if (consumed == false) {
            consumed = true;
            mConsumer.accept(t);
        }
    }
    
}
