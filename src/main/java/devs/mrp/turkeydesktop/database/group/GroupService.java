package devs.mrp.turkeydesktop.database.group;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface GroupService {
    public Single<Long> add(Group element);
    public Single<Long> update(Group element);
    public Observable<Group> findAll();
    public Maybe<Group> findById(long id);
    public Single<Long> deleteById(long id);
    
    public Observable<Group> findAllPositive();
    public Observable<Group> findAllNegative();
    
    public Single<Integer> setPreventClose(long groupId, boolean preventClose);
    public Single<Boolean> isPreventClose(long groupId);
    public Single<Boolean> isDisablePoints(long groupId);
}
