/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.util.function.LongConsumer;

/**
 *
 * @author ncm55070
 */
public class SingleLongConsumer implements LongConsumer {

    private boolean consumed = false;
    private final LongConsumer mConsumer;
    
    public SingleLongConsumer(LongConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void accept(long t) {
        if (consumed == false) {
            consumed = true;
            mConsumer.accept(t);
        }
    }
    
}
