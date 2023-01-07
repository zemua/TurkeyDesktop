/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author miguel
 */
public class ExportedGroupFactory {
    
    public static ExportedGroupService getService() {
        return new ExportedGroupServiceImpl();
    }
    
    public static void runExportedGroupWorker(Supplier<ExportedGroup> supplier, Consumer<ExportedGroup> consumer) {
        new GenericWorker<ExportedGroup>().runWorker(supplier, consumer);
    }
    
    public static void runExportedGroupListWorker(Supplier<List<ExportedGroup>> supplier, Consumer<List<ExportedGroup>> consumer) {
        new GenericWorker<List<ExportedGroup>>().runWorker(supplier, consumer);
    }
    
    public static Consumer<ExportedGroup> exportedGroupConsumer(Consumer<ExportedGroup> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<ExportedGroup>> exportedGroupListConsumer(Consumer<List<ExportedGroup>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
