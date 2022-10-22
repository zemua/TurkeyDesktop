/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface ConditionChecker {
    
    public void isConditionMet(Condition condition, Consumer<Boolean> consumer);
    public void areConditionsMet(List<Condition> conditions, Consumer<Boolean> consumer);
    public void areConditionsMet(long groupId, Consumer<Boolean> consumer);
    public void isLockDownTime(Consumer<Boolean> consumer);
    public void isLockDownTime(Long now, Consumer<Boolean> consumer);
    public void isTimeRunningOut(Consumer<Boolean> consumer);
    public void timeRemaining(LongConsumer consumer);
    public void isIdle(Consumer<Boolean> consumer);
    public void isIdleWithToast(Consumer<Boolean> consumer);
    public void notifyCloseToConditionsRefresh();
    
}
