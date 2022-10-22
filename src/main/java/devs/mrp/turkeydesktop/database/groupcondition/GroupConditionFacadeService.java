/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.group.GroupServiceFactory;
import java.util.List;
import devs.mrp.turkeydesktop.database.group.GroupService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class GroupConditionFacadeService implements IGroupConditionFacadeService {
    
    private final IConditionService conditionService = FConditionService.getService();
    private final GroupService groupService = GroupServiceFactory.getService();
    
    @Override
    public void findByConditionId(long conditionId, Consumer<GroupConditionFacade> c) {
        var consumer = FGroupConditionFacadeService.getConsumer(c);
        conditionService.findById(conditionId, condition -> {
            toFacade(condition, facade -> {
                consumer.accept(facade);
            });
        });
    }

    @Override
    public void findByGroupId(long groupId, Consumer<List<GroupConditionFacade>> c) {
        var consumer = FGroupConditionFacadeService.getListConsumer(c);
        List<GroupConditionFacade> list = Collections.synchronizedList(new ArrayList<>());
        conditionService.findByGroupId(groupId, result -> {
            result.forEach(con -> {
                toFacade(con, facade -> {
                    list.add(facade);
                    if (list.size() == result.size()) {
                        consumer.accept(list);
                    }
                });
            });
        });
    }
    
    private void toFacade(Condition condition, Consumer<GroupConditionFacade> c) {
        var consumer = FGroupConditionFacadeService.getConsumer(c);
        groupService.findById(condition.getGroupId(), origin -> {
            groupService.findById(condition.getTargetId(), target -> {
                GroupConditionFacade facade = new GroupConditionFacade();
                facade.setConditionId(condition.getId());
                facade.setGroupId(condition.getGroupId());
                facade.setGroupName(origin.getName());
                facade.setTargetId(condition.getTargetId());
                facade.setTargetName(target.getName());
                facade.setLastDaysCondition(condition.getLastDaysCondition());
                facade.setUsageTimeCondition(condition.getUsageTimeCondition());
                consumer.accept(facade);
            });
        });
    }
    
}
