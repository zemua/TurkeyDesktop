/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface AssignableElementService<TYPE> {
    public Observable<List<AssignableElement<TYPE>>> positiveElementsWithAssignation();
    public Observable<List<AssignableElement<TYPE>>> negativeElementsWithAssignation();
}
