package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public class GroupConditionFacadeServiceImpl implements GroupConditionFacadeService {
    
    private final GroupConditionFacadeFactory factory;
    
    private final ConditionService conditionService;
    private final GroupService groupService;
    
    public GroupConditionFacadeServiceImpl(GroupConditionFacadeFactory factory) {
        this.factory = factory;
        this.conditionService = factory.getConditionService();
        this.groupService = factory.getGroupService();
    }
    
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
            GroupConditionFacade facade = factory.createFacade();
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
