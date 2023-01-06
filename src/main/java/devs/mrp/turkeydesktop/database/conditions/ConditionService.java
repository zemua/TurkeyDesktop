/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface ConditionService {
    public Single<Long> add(Condition element);
    public Single<Long> update(Condition element);
    public Observable<Condition> findAll();
    public Observable<Condition> findByGroupId(Long groupId);
    public Maybe<Condition> findById(Long id);
    public Single<Long> deleteById(Long id);
    public Single<Long> deleteByGroupId(long id);
    public Single<Long> deleteByTargetId(long id);
}
