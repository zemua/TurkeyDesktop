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
import rx.Single;

/**
 *
 * @author miguel
 */
public interface ConditionChecker {
    
    public void isConditionMet(Condition condition, Consumer<Boolean> consumer);
    public Single<Boolean> areConditionsMet(List<Condition> conditions);
    public Single<Boolean> areConditionsMet(long groupId);
    public Single<Boolean> isLockDownTime();
    public void isLockDownTime(Long now, Consumer<Boolean> consumer);
    public void isTimeRunningOut(Consumer<Boolean> consumer);
    public void timeRemaining(LongConsumer consumer);
    public Single<Boolean> isIdle();
    public Single<Boolean> isIdleWithToast(boolean sendToast);
    public void notifyCloseToConditionsRefresh();
    
}
