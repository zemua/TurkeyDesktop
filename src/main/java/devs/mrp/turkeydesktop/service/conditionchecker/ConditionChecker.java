/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import java.util.List;

/**
 *
 * @author miguel
 */
public interface ConditionChecker {
    
    public boolean isConditionMet(Condition condition);
    public boolean areConditionsMet(List<Condition> conditions);
    public boolean areConditionsMet(long groupId);
    public boolean isLockDownTime();
    public boolean isLockDownTime(Long now);
    public boolean isTimeRunningOut();
    public long timeRemaining();
    public boolean isIdle();
    public boolean isIdleWithToast();
    public void notifyCloseToConditionsRefresh();
    
}
