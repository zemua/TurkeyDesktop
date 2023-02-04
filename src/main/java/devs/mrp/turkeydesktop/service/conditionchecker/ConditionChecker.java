package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

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
