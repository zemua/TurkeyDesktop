/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author miguel
 */
public class TitleServiceFactory {
    
    public static TitleService getService() {
        return new TitleServiceImpl();
    }
    
    public static void runTitleWorker(Supplier<Title> supplier, Consumer<Title> consumer) {
        new GenericWorker<Title>().runWorker(supplier, consumer);
    }
    
    public static void runTitleListWorker(Supplier<List<Title>> supplier, Consumer<List<Title>> consumer) {
        new GenericWorker<List<Title>>().runWorker(supplier, consumer);
    }
    
    public static Consumer<Title> getConsumer(Consumer<Title> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<Title>> getListConsumer(Consumer<List<Title>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
