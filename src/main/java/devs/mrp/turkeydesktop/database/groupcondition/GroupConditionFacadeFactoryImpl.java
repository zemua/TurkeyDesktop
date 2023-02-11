package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.group.GroupService;
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
        return new GroupConditionFacadeServiceImpl(this);
    }
    
    @Override
    public Consumer<GroupConditionFacade> getConsumer(Consumer<GroupConditionFacade> consumer) {
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
    @Override
    public Consumer<List<GroupConditionFacade>> getListConsumer(Consumer<List<GroupConditionFacade>> consumer) {
        return new SingleConsumer<>(consumer, factory.getToaster());
    }

    @Override
    public ConditionChecker conditionCheker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
    }

    @Override
    public GroupConditionFacade createFacade() {
        return new GroupConditionFacade(this);
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public ConditionService getConditionService() {
        return factory.getConditionFactory().getService();
    }

    @Override
    public GroupService getGroupService() {
        return factory.getGroupFactory().getService();
    }
    
}
