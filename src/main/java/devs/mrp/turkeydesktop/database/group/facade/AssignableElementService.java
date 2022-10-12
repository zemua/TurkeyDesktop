/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public interface AssignableElementService<TYPE> {
    public void positiveElementsWithAssignation(Consumer<List<AssignableElement<TYPE>>> consumer);
    public void negativeElementsWithAssignation(Consumer<List<AssignableElement<TYPE>>> consumer);
}
