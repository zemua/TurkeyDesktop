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
public class AssignableElementService implements IAssignableElementService {
    
    private ITypeService typeService = FTypeService.getService();
    private IGroupAssignationService assignationService = FGroupAssignationService.getService();
    
    @Override
    public List<AssignableElement> positiveProcessesWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.PROCESS),
                Type.Types.POSITIVE,
                GroupAssignation.ElementType.PROCESS);
    }

    @Override
    public List<AssignableElement> negativeProcessesWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.PROCESS),
                Type.Types.NEGATIVE,
                GroupAssignation.ElementType.PROCESS);
    }

    @Override
    public List<AssignableElement> positiveTitlesWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.TITLE),
                Type.Types.POSITIVE,
                GroupAssignation.ElementType.TITLE);
    }

    @Override
    public List<AssignableElement> negativeTitlesWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.TITLE),
                Type.Types.NEGATIVE,
                GroupAssignation.ElementType.TITLE);
    }
    
    private List<AssignableElement> elementsWithAssignation(Map<String, GroupAssignation> assignables, Type.Types positiveOrNegative, GroupAssignation.ElementType processOrTitle) {
        return typeService.findAll()
                .stream()
                .filter(t -> t.getType().equals(positiveOrNegative))
                .map(t -> {
                    AssignableElement element = new AssignableElement();
                    element.setElementName(t.getProcess());
                    if (assignables.get(t.getProcess()) != null) {
                        element.setGroupAssignationId(assignables.get(t.getProcess()).getId());
                        element.setGroupId(assignables.get(t.getProcess()).getGroupId());
                    } else {
                        element.setGroupAssignationId(null);
                        element.setGroupId(null);
                    }
                    element.setPositiveOrNegative(positiveOrNegative);
                    element.setProcessOrTitle(processOrTitle);
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
