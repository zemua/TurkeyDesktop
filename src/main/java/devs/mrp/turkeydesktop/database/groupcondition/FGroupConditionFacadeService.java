/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class FGroupConditionFacadeService {
    
    public static IGroupConditionFacadeService getService() {
        return new GroupConditionFacadeService();
    }
    
    public static Consumer<GroupConditionFacade> getConsumer(Consumer<GroupConditionFacade> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<GroupConditionFacade>> getListConsumer(Consumer<List<GroupConditionFacade>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
