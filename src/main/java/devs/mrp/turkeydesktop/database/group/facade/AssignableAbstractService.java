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
import java.util.stream.Collectors;

/**
 *
 * @author miguel
 */
public abstract class AssignableAbstractService {
    
    private IGroupAssignationService assignationService = FGroupAssignationService.getService();
    
    protected Map<String, GroupAssignation> getAssignationsMap(GroupAssignation.ElementType type) {
        return assignationService
                .findAll()
                .stream()
                .filter(a -> a.getType().equals(type))
                .collect(Collectors.toMap((a -> a.getElementId()),(a -> a)));
    }
}
