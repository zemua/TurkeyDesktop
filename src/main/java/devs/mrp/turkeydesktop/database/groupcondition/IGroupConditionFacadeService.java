/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface IGroupConditionFacadeService {
    
    public Single<GroupConditionFacade> findByConditionId(long conditionId);
    public Observable<GroupConditionFacade> findByGroupId(long groupId);
    
}
