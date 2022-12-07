/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import java.util.List;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface ConditionChecker {
    
    public Single<Boolean> isConditionMet(Condition condition);
    public Single<Boolean> areConditionsMet(List<Condition> conditions);
    public Single<Boolean> areConditionsMet(long groupId);
    public Single<Boolean> isLockDownTime();
    public Single<Boolean> isLockDownTime(Long now);
    public Single<Boolean> isTimeRunningOut();
    public Single<Long> timeRemaining();
    public Single<Boolean> isIdle();
    public Single<Boolean> isIdleWithToast(boolean sendToast);
    public Single<Boolean> notifyCloseToConditionsRefresh();
    
}
