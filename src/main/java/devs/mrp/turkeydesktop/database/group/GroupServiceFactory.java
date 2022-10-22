/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author miguel
 */
public class GroupServiceFactory {
    
    public static GroupService getService() {
        return new GroupServiceImpl();
    }
    
    public static void runGroupWorker(Supplier<Group> supplier, Consumer<Group> consumer) {
        new GenericWorker<Group>().runWorker(supplier, consumer);
    }
    
    public static void runGroupListWorker(Supplier<List<Group>> supplier, Consumer<List<Group>> consumer) {
        new GenericWorker<List<Group>>().runWorker(supplier, consumer);
    }
    
    public static Consumer<Group> groupConsumer(Consumer<Group> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<Group>> groupListConsumer(Consumer<List<Group>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
