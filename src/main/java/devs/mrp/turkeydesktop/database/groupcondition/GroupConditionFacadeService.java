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
import devs.mrp.turkeydesktop.database.group.GroupService;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public class GroupConditionFacadeService implements IGroupConditionFacadeService {
    
    private final IConditionService conditionService = FConditionService.getService();
    private final GroupService groupService = GroupServiceFactory.getService();
    
    @Override
    public Single<GroupConditionFacade> findByConditionId(long conditionId) {
        return conditionService.findById(conditionId).flatMap(this::toFacade);
    }

    @Override
    public Observable<GroupConditionFacade> findByGroupId(long groupId) {
        return conditionService.findByGroupId(groupId).flatMapSingle(this::toFacade);
    }
    
    private Single<GroupConditionFacade> toFacade(Condition condition) {
        return groupService.findById(condition.getGroupId()).flatMap(origin -> {
            return groupService.findById(condition.getTargetId()).map(target -> {
                GroupConditionFacade facade = new GroupConditionFacade();
                facade.setConditionId(condition.getId());
                facade.setGroupId(condition.getGroupId());
                facade.setGroupName(origin.getName());
                facade.setTargetId(condition.getTargetId());
                facade.setTargetName(target.getName());
                facade.setLastDaysCondition(condition.getLastDaysCondition());
                facade.setUsageTimeCondition(condition.getUsageTimeCondition());
                return facade;
            });
        });
    }
    
}
