/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
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
}
