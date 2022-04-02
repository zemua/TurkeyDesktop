/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import devs.mrp.turkeydesktop.database.type.TypeService;

/**
 *
 * @author miguel
 */
public class AssignableProcessService extends AssignableAbstractService implements IAssignableElementService<Type.Types> {
    
    private final TypeService typeService = TypeServiceFactory.getService();
    private static final Logger logger = Logger.getLogger(AssignableProcessService.class.getName());
    
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
                        //element.setGroupAssignationId(assignables.get(t.getProcess()).getId());
                        element.setGroupId(assignables.get(t.getProcess()).getGroupId());
                    } else {
                        //element.setGroupAssignationId(null);
                        element.setGroupId(null);
                    }
                    element.setPositiveOrNegative(positiveOrNegative);
                    element.setProcessOrTitle(GroupAssignation.ElementType.PROCESS);
                    return element;
                })
                .collect(Collectors.toList());
    }
    
}
