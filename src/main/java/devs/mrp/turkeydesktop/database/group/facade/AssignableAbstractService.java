package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;

public abstract class AssignableAbstractService {
    
    private GroupAssignationService assignationService;
    
    public AssignableAbstractService(AssignableElementFactory assignableFactory) {
        this.assignationService = assignableFactory.getAssignationService();
    }
    
    protected Single<Map<String, GroupAssignation>> getAssignationsMap(GroupAssignation.ElementType type) {
        return assignationService.findAll()
                .filter(a -> type.equals(a.getType()))
                .toMap((a -> a.getElementId()),(a -> a))
                .toMaybe()
                .toSingle();
    }
}
