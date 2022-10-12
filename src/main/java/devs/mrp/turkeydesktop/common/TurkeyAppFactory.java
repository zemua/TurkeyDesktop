/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.util.concurrent.ExecutionException;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author ncm55070
 */
public class TurkeyAppFactory {
    
    public static SwingWorker<Long, Object> getLongWorker(LongSupplier longSupplier, LongConsumer longConsumer) {
        return new SwingWorker<Long, Object>() {
            @Override
            protected Long doInBackground() throws Exception {
                return longSupplier.getAsLong();
            }
            @Override
            protected void done() {
                try {
                    longConsumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }
    
    public static SwingWorker<Object, Object> getWorker(Runnable runnable) {
        return new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                runnable.run();
                return null;
            }
        };
    }
}
