/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import java.util.Map;
import io.reactivex.rxjava3.core.Single;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;

/**
 *
 * @author miguel
 */
public abstract class AssignableAbstractService {
    
    private GroupAssignationService assignationService = GroupAssignationFactory.getService();
    
    protected Single<Map<String, GroupAssignation>> getAssignationsMap(GroupAssignation.ElementType type) {
        return assignationService.findAll()
                .filter(a -> type.equals(a.getType()))
                .toMap((a -> a.getElementId()),(a -> a))
                .toMaybe()
                .toSingle();
    }
}
