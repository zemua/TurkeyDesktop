/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.type.FTypeService;
import devs.mrp.turkeydesktop.database.type.ITypeService;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author miguel
 */
public class AssignableProcessService implements IAssignableElementService<Type.Types> {
    
    private ITypeService typeService = FTypeService.getService();
    private IGroupAssignationService assignationService = FGroupAssignationService.getService();
    
    @Override
    public List<AssignableElement<Type.Types>> positiveElementsWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.PROCESS),
                Type.Types.POSITIVE);
    }

    @Override
    public List<AssignableElement<Type.Types>> negativeElementsWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.PROCESS),
                Type.Types.NEGATIVE);
    }
    
    private List<AssignableElement<Type.Types>> elementsWithAssignation(Map<String, GroupAssignation> assignables, Type.Types positiveOrNegative) {
        return typeService.findAll()
                .stream()
                .filter(t -> t.getType().equals(positiveOrNegative))
                .map(t -> {
                    AssignableElement<Type.Types> element = new AssignableElement<>();
                    element.setElementName(t.getProcess());
                    if (assignables.get(t.getProcess()) != null) {
                        element.setGroupAssignationId(assignables.get(t.getProcess()).getId());
                        element.setGroupId(assignables.get(t.getProcess()).getGroupId());
                    } else {
                        element.setGroupAssignationId(null);
                        element.setGroupId(null);
                    }
                    element.setPositiveOrNegative(positiveOrNegative);
                    element.setProcessOrTitle(GroupAssignation.ElementType.PROCESS);
                    return element;
                })
                .collect(Collectors.toList());
    }
    
    private Map<String, GroupAssignation> getAssignationsMap(GroupAssignation.ElementType type) {
        return assignationService
                .findAll()
                .stream()
                .filter(a -> a.getType().equals(type))
                .collect(Collectors.toMap((a -> a.getElementId()),(a -> a)));
    }
    
}
