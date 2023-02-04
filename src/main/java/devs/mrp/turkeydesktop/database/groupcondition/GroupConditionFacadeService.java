/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

/**
 *
 * @author miguel
 */
public interface GroupConditionFacadeService {
    
    public Maybe<GroupConditionFacade> findByConditionId(long conditionId);
    public Observable<GroupConditionFacade> findByGroupId(long groupId);
    
}
