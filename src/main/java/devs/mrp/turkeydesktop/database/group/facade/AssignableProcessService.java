/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.Map;
import java.util.logging.Logger;
import devs.mrp.turkeydesktop.database.type.TypeService;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class AssignableProcessService extends AssignableAbstractService implements AssignableElementService<Type.Types> {
    
    private final TypeService typeService = TypeServiceFactory.getService();
    private static final Logger logger = Logger.getLogger(AssignableProcessService.class.getName());
    
    @Override
    public Observable<AssignableElement<Type.Types>> positiveElementsWithAssignation() {
        return getAssignationsMap(GroupAssignation.ElementType.PROCESS).flatMapObservable(result -> elementsWithAssignation(result, Type.Types.POSITIVE));
    }

    @Override
    public Observable<AssignableElement<Type.Types>> negativeElementsWithAssignation() {
        return getAssignationsMap(GroupAssignation.ElementType.PROCESS).flatMapObservable(result -> elementsWithAssignation(result, Type.Types.NEGATIVE));
    }
    
    private Observable<AssignableElement<Type.Types>> elementsWithAssignation(Map<String, GroupAssignation> assignables, Type.Types positiveOrNegative) {
        return typeService.findAll()
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
                });
    }
    
}
