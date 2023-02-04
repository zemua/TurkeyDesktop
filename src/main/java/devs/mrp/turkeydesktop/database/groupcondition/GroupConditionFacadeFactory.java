package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import java.util.List;
import java.util.function.Consumer;

public interface GroupConditionFacadeFactory {
    GroupConditionFacade createFacade();
    ConditionChecker conditionCheker();
    GroupConditionFacadeService getService();
    Consumer<GroupConditionFacade> getConsumer(Consumer<GroupConditionFacade> consumer);
    Consumer<List<GroupConditionFacade>> getListConsumer(Consumer<List<GroupConditionFacade>> consumer);
}
