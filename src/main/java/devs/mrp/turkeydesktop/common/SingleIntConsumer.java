/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.util.function.IntConsumer;

/**
 *
 * @author ncm55070
 */
public class SingleIntConsumer implements IntConsumer {
    
    private boolean consumed = false;
    private final IntConsumer mConsumer;
    
    public SingleIntConsumer(IntConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void accept(int t) {
        if (consumed == false) {
            consumed = true;
            mConsumer.accept(t);
        }
    }
    
}
