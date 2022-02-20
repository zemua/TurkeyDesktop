/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface IGroupConditionFacadeService {
    
    public GroupConditionFacade findByConditionId(long conditionId);
    public List<GroupConditionFacade> findByGroupId(long groupId);
    
}
