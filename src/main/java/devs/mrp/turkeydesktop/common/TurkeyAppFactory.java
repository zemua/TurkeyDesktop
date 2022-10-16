/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author ncm55070
 */
public class TurkeyAppFactory {
    
    public static void runLongWorker(LongSupplier longSupplier, LongConsumer longConsumer) {
        var worker = new SwingWorker<Long, Object>() {
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
        worker.execute();
    }
    
    public static void runIntWorker(IntSupplier intSupplier, IntConsumer intConsumer) {
        var worker = new SwingWorker<Integer, Object>() {
            @Override
            protected Integer doInBackground() throws Exception {
                return intSupplier.getAsInt();
            }
            @Override
            protected void done() {
                try {
                    intConsumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
    
    public static void runWorker(Runnable runnable) {
        runInNewThread(runnable);
    }
    
    public static void runResultSetWorker(Supplier<ResultSet> supplier, Consumer<ResultSet> consumer) {
        var worker = new SwingWorker<ResultSet, Object>() {
            @Override
            protected ResultSet doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
    
    public static void runInNewThread(Runnable r) {
        new Thread(r).start();
    }
    
    public static ExecutorService getSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
    
    public static void runBooleanWorker(Supplier<Boolean> supplier, Consumer<Boolean> consumer) {
        var worker = new SwingWorker<Boolean, Object>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(TurkeyAppFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
}
