/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

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
public class GroupAssignationFactory {
    
    private static Supplier<IGroupAssignationService> groupAssignationServiceSupplier;

    public static void setGroupAssignationServiceSupplier(Supplier<IGroupAssignationService> groupAssignationServiceSupplier) {
        GroupAssignationFactory.groupAssignationServiceSupplier = groupAssignationServiceSupplier;
    }
    
    public static IGroupAssignationService getService() {
        return groupAssignationServiceSupplier.get();
    }
    
    public static void runGroupAssignationWorker(Supplier<GroupAssignation> supplier, Consumer<GroupAssignation> consumer) {
        var worker = new SwingWorker<GroupAssignation, Object>() {
            @Override
            protected GroupAssignation doInBackground() throws Exception {
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
    
    public static void runGroupAssignationListWoker(Supplier<List<GroupAssignation>> supplier, Consumer<List<GroupAssignation>> consumer) {
        var worker = new SwingWorker<List<GroupAssignation>, Object>() {
            @Override
            protected List<GroupAssignation> doInBackground() throws Exception {
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
    
    public static Consumer<GroupAssignation> groupAssignationConsumer(Consumer<GroupAssignation> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<GroupAssignation>> groupAssignationListConsumer(Consumer<List<GroupAssignation>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
