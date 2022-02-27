/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.group.FGroupService;
import devs.mrp.turkeydesktop.database.group.IGroupService;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import java.util.List;

/**
 *
 * @author miguel
 */
public class ConditionChecker implements IConditionChecker {
    
    private IConditionService conditionService = FConditionService.getService();
    private IGroupService groupService = FGroupService.getService();
    private ITimeLogService timeLogService = FTimeLogService.getService();

    @Override
    public boolean isConditionMet(Condition condition) {
        long timeSpent = timeLogService.timeSpentOnGroupForFrame(condition.getTargetId(),
                TimeConverter.beginningOfOffsetDays(condition.getLastDaysCondition()),
                TimeConverter.endOfToday());
        return timeSpent >= condition.getUsageTimeCondition();
    }

    @Override
    public boolean areConditionsMet(List<Condition> conditions) {
        return !isLockDownTime() && conditions.stream()
                .map(c -> isConditionMet(c))
                .allMatch(b -> b.equals(true));
    }

    @Override
    public boolean areConditionsMet(long groupId) {
        if (groupId <= 0){
            return true;
        }
        List<Condition> conditions = conditionService.findByGroupId(groupId);
        return areConditionsMet(conditions);
    }
    
    @Override
    public boolean isLockDownTime() {
        // TODO
        return false;
    }
    
}
