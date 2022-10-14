/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author ncm55070
 */
public class GenericWorker<T> {
    public void runWorker(Supplier<T> supplier, Consumer<T> consumer) {
        var worker = new SwingWorker<T, Object>() {
            @Override
            protected T doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(GenericWorker.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(GenericWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
}
