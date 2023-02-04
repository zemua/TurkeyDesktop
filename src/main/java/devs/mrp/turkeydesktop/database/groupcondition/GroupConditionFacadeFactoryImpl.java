package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import java.util.List;
import java.util.function.Consumer;

public class GroupConditionFacadeFactoryImpl implements GroupConditionFacadeFactory {
    
    private FactoryInitializer factory;
    
    public GroupConditionFacadeFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public GroupConditionFacadeService getService() {
        return new GroupConditionFacadeServiceImpl();
    }
    
    @Override
    public Consumer<GroupConditionFacade> getConsumer(Consumer<GroupConditionFacade> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    @Override
    public Consumer<List<GroupConditionFacade>> getListConsumer(Consumer<List<GroupConditionFacade>> consumer) {
        return new SingleConsumer<>(consumer);
    }

    @Override
    public ConditionChecker conditionCheker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
    }
    
}
