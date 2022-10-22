/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class ExternalGroupServiceFactory {
    
    public static ExternalGroupService getService() {
        return new ExternalGroupServiceImpl();
    }
    
    public static Consumer<ExternalGroup> externalGroupConsumer(Consumer<ExternalGroup> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<ExternalGroup>> externalGroupListConsumer(Consumer<List<ExternalGroup>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
}
