package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeService;
import io.reactivex.rxjava3.core.Observable;
import java.util.Map;

public class AssignableProcessService extends AssignableAbstractService implements AssignableElementService<Type.Types> {
    
    private final TypeService typeService;
    
    public AssignableProcessService(AssignableElementFactory factory) {
        super(factory);
        this.typeService = factory.getTypeService();
    }
    
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
                        element.setGroupId(assignables.get(t.getProcess()).getGroupId());
                    } else {
                        element.setGroupId(null);
                    }
                    element.setPositiveOrNegative(positiveOrNegative);
                    element.setProcessOrTitle(GroupAssignation.ElementType.PROCESS);
                    return element;
                });
    }
    
}
