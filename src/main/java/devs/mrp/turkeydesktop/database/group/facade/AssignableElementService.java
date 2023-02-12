package devs.mrp.turkeydesktop.database.group.facade;

import io.reactivex.rxjava3.core.Observable;

public interface AssignableElementService<TYPE> {
    public Observable<AssignableElement<TYPE>> positiveElementsWithAssignation();
    public Observable<AssignableElement<TYPE>> negativeElementsWithAssignation();
}
