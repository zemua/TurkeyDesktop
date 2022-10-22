/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.database.titles.Title;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class AssignableElementServiceFactory {
    
    public static AssignableElementService getProcessesService() {
        return new AssignableProcessService();
    }
    
    public static AssignableElementService getTitlesService() {
        return new AssignableTitleServiceImpl();
    }
    
    public static Consumer<List<AssignableElement<Title.Type>>> getConsumer(Consumer<List<AssignableElement<Title.Type>>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
