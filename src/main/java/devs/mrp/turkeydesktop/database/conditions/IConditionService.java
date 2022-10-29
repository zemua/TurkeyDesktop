/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface IConditionService {
    public Observable<Long> add(Condition element);
    public Observable<Long> update(Condition element);
    public Observable<List<Condition>> findAll();
    public Observable<List<Condition>> findByGroupId(Long groupId);
    public Observable<Condition> findById(Long id);
    public Observable<Long> deleteById(Long id);
    public Observable<Long> deleteByGroupId(long id);
    public Observable<Long> deleteByTargetId(long id);
}
