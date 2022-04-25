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
import devs.mrp.turkeydesktop.database.group.Group;
import java.util.List;
import java.util.stream.Collectors;
import devs.mrp.turkeydesktop.database.group.GroupService;

/**
 *
 * @author miguel
 */
public class GroupConditionFacadeService implements IGroupConditionFacadeService {
    
    private final IConditionService conditionService = FConditionService.getService();
    private final GroupService groupService = GroupServiceFactory.getService();
    
    @Override
    public GroupConditionFacade findByConditionId(long conditionId) {
        Condition condition = conditionService.findById(conditionId);
        return toFacade(condition);
    }

    @Override
    public List<GroupConditionFacade> findByGroupId(long groupId) {
        return conditionService.findByGroupId(groupId)
                .stream()
                .map(condition -> toFacade(condition))
                .collect(Collectors.toList());
    }
    
    private GroupConditionFacade toFacade(Condition condition) {
        Group origin = groupService.findById(condition.getGroupId());
        Group target = groupService.findById(condition.getTargetId());
        GroupConditionFacade facade = new GroupConditionFacade();
        facade.setConditionId(condition.getId());
        facade.setGroupId(condition.getGroupId());
        facade.setGroupName(origin.getName());
        facade.setTargetId(condition.getTargetId());
        facade.setTargetName(target.getName());
        facade.setLastDaysCondition(condition.getLastDaysCondition());
        facade.setUsageTimeCondition(condition.getUsageTimeCondition());
        return facade;
    }
    
}
