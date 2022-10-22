/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author miguel
 */
public class CloseableServiceFactory {
    
    public static CloseableService getService() {
        return new CloseableServiceImpl();
    }
    
    public static void runCloseableListWorker(Supplier<List<Closeable>> supplier, Consumer<List<Closeable>> consumer) {
        var worker = new SwingWorker<List<Closeable>, Object>() {
            @Override
            protected List<Closeable> doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(WorkerFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(WorkerFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
    
    public static void runCloseableWorker(Supplier<Closeable> supplier, Consumer<Closeable> consumer) {
        var worker = new SwingWorker<Closeable, Object>() {
            @Override
            protected Closeable doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(WorkerFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(WorkerFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
    
    public static Consumer<List<Closeable>> singleListConsumer(Consumer<List<Closeable>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<Closeable> singleConsumer(Consumer<Closeable> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
