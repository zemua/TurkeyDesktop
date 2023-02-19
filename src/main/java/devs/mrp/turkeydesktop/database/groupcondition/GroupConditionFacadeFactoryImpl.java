package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactoryImpl;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.group.GroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactoryImpl;
import java.util.List;
import java.util.function.Consumer;

public class GroupConditionFacadeFactoryImpl implements GroupConditionFacadeFactory {
    
    private static GroupConditionFacadeFactoryImpl instance;
    
    private GroupConditionFacadeFactoryImpl() {}
    
    public static GroupConditionFacadeFactoryImpl getInstance() {
        if (instance == null) {
            instance = new GroupConditionFacadeFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public GroupConditionFacadeService getService() {
        return new GroupConditionFacadeServiceImpl(this);
    }
    
    @Override
    public Consumer<GroupConditionFacade> getConsumer(Consumer<GroupConditionFacade> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
    @Override
    public Consumer<List<GroupConditionFacade>> getListConsumer(Consumer<List<GroupConditionFacade>> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }

    @Override
    public ConditionChecker conditionCheker() {
        return ConditionCheckerFactoryImpl.getInstance().getConditionChecker();
    }

    @Override
    public GroupConditionFacade createFacade() {
        return new GroupConditionFacade(this);
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }

    @Override
    public ConditionService getConditionService() {
        return ConditionFactoryImpl.getInstance().getService();
    }

    @Override
    public GroupService getGroupService() {
        return GroupFactoryImpl.getInstance().getService();
    }
    
}
