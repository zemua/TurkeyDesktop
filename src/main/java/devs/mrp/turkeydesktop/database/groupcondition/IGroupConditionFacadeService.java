/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public interface IGroupConditionFacadeService {
    
    public GroupConditionFacade findByConditionId(long conditionId);
    public void findByGroupId(long groupId, Consumer<List<GroupConditionFacade>> consumer);
    
}
