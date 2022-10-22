/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

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
public class FConditionService {
    
    public static IConditionService getService() {
        return new ConditionService();
    }
    
    public static void runConditionListWorker(Supplier<List<Condition>> supplier, Consumer<List<Condition>> consumer) {
        var worker = new SwingWorker<List<Condition>, Object>() {
            @Override
            protected List<Condition> doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(FConditionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(FConditionService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
    
    public static void runConditionWorker(Supplier<Condition> supplier, Consumer<Condition> consumer) {
        var worker = new SwingWorker<Condition, Object>() {
            @Override
            protected Condition doInBackground() throws Exception {
                return supplier.get();
            }
            @Override
            protected void done() {
                try {
                    consumer.accept(get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(FConditionService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(FConditionService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        worker.execute();
    }
    
}
