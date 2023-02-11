package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import io.reactivex.rxjava3.core.Observable;
import java.util.Map;

public class AssignableTitleServiceImpl extends AssignableAbstractService implements AssignableElementService<Title.Type> {
    
    private final TitleService titleService;
    
    public AssignableTitleServiceImpl(AssignableElementFactory factory) {
        super(factory);
        this.titleService = factory.getTitleService();
    }
    
    @Override
    public Observable<AssignableElement<Title.Type>> positiveElementsWithAssignation() {
        return getAssignationsMap(GroupAssignation.ElementType.TITLE).flatMapObservable(result -> elementsWithAssignation(result, Title.Type.POSITIVE));
    }

    @Override
    public Observable<AssignableElement<Title.Type>> negativeElementsWithAssignation() {
        return getAssignationsMap(GroupAssignation.ElementType.TITLE).flatMapObservable(result -> elementsWithAssignation(result, Title.Type.NEGATIVE));
    }
    
    private Observable<AssignableElement<Title.Type>> elementsWithAssignation(Map<String, GroupAssignation> assignables, Title.Type positiveOrNegative) {
        return titleService.findAll()
                .filter(t -> t.getType().equals(positiveOrNegative))
                .map(t -> {
                    AssignableElement<Title.Type> element = new AssignableElement<>();
                    element.setElementName(t.getSubStr());
                    if (assignables.get(t.getSubStr()) != null) {
                        element.setGroupId(assignables.get(t.getSubStr()).getGroupId());
                    } else {
                        element.setGroupId(null);
                    }
                    element.setPositiveOrNegative(positiveOrNegative);
                    element.setProcessOrTitle(GroupAssignation.ElementType.TITLE);
                    return element;
                });
    }
    
}
