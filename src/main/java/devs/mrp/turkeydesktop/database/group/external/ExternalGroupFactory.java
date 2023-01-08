package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import java.util.List;
import java.util.function.Consumer;

public class ExternalGroupFactory {
    
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
