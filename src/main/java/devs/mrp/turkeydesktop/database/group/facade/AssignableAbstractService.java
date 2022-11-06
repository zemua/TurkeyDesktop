/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import java.util.Map;
import rx.Single;

/**
 *
 * @author miguel
 */
public abstract class AssignableAbstractService {
    
    private IGroupAssignationService assignationService = FGroupAssignationService.getService();
    
    protected Single<Map<String, GroupAssignation>> getAssignationsMap(GroupAssignation.ElementType type) {
        return assignationService.findAll()
                .filter(a -> type.equals(a.getType()))
                .toMap((a -> a.getElementId()),(a -> a))
                .toSingle();
    }
}
