/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import devs.mrp.turkeydesktop.database.type.TypeService;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class AssignableProcessService extends AssignableAbstractService implements AssignableElementService<Type.Types> {
    
    private final TypeService typeService = TypeServiceFactory.getService();
    private static final Logger logger = Logger.getLogger(AssignableProcessService.class.getName());
    
    @Override
    public void positiveElementsWithAssignation(Consumer<List<AssignableElement<Type.Types>>> consumer) {
        getAssignationsMap(GroupAssignation.ElementType.PROCESS, result -> {
            elementsWithAssignation(result, Type.Types.POSITIVE, consumer);
        });
    }

    @Override
    public void negativeElementsWithAssignation(Consumer<List<AssignableElement<Type.Types>>> consumer) {
        getAssignationsMap(GroupAssignation.ElementType.PROCESS, result -> {
            elementsWithAssignation(result, Type.Types.NEGATIVE, consumer);
        });
    }
    
    private void elementsWithAssignation(Map<String, GroupAssignation> assignables, Type.Types positiveOrNegative, Consumer<List<AssignableElement<Type.Types>>> consumer) {
        typeService.findAll(result -> {
            var computed = result.stream()
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
            consumer.accept(computed);
        });
    }
    
}
