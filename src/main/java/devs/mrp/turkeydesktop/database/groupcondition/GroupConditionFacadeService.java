/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactory;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupServiceFactory;
import devs.mrp.turkeydesktop.database.group.GroupService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;

/**
 *
 * @author miguel
 */
public class GroupConditionFacadeService implements IGroupConditionFacadeService {
    
    private final ConditionService conditionService = ConditionFactory.getService();
    private final GroupService groupService = GroupServiceFactory.getService();
    
    @Override
    public Maybe<GroupConditionFacade> findByConditionId(long conditionId) {
        return conditionService.findById(conditionId)
                .flatMap(this::toFacade);
    }

    @Override
    public Observable<GroupConditionFacade> findByGroupId(long groupId) {
        return conditionService.findByGroupId(groupId).flatMapMaybe(this::toFacade);
    }
    
    private Maybe<GroupConditionFacade> toFacade(Condition condition) {
        Maybe<Group> ori = groupService.findById(condition.getGroupId());
        Maybe<Group> tar = groupService.findById(condition.getTargetId());
        return Maybe.zip(ori, tar, (origin, target) -> {
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
    }
    
}
