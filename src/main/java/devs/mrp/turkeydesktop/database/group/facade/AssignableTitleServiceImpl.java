/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.titles.TitleServiceFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class AssignableTitleServiceImpl extends AssignableAbstractService implements AssignableElementService<Title.Type> {
    
    private final TitleService titleService = TitleServiceFactory.getService();
    
    @Override
    public void positiveElementsWithAssignation(Consumer<List<AssignableElement<Title.Type>>> consumer) {
        getAssignationsMap(GroupAssignation.ElementType.TITLE, result -> {
            consumer.accept(elementsWithAssignation(result, Title.Type.POSITIVE));
        });
    }

    @Override
    public void negativeElementsWithAssignation(Consumer<List<AssignableElement<Title.Type>>> consumer) {
        getAssignationsMap(GroupAssignation.ElementType.TITLE, result -> {
            consumer.accept(elementsWithAssignation(result, Title.Type.NEGATIVE));
        });
    }
    
    private List<AssignableElement<Title.Type>> elementsWithAssignation(Map<String, GroupAssignation> assignables, Title.Type positiveOrNegative) {
        return titleService.findAll()
                .stream()
                .filter(t -> t.getType().equals(positiveOrNegative))
                .map(t -> {
                    AssignableElement<Title.Type> element = new AssignableElement<>();
                    element.setElementName(t.getSubStr());
                    if (assignables.get(t.getSubStr()) != null) {
                        //element.setGroupAssignationId(assignables.get(t.getSubStr()).getId());
                        element.setGroupId(assignables.get(t.getSubStr()).getGroupId());
                    } else {
                        //element.setGroupAssignationId(null);
                        element.setGroupId(null);
                    }
                    element.setPositiveOrNegative(positiveOrNegative);
                    element.setProcessOrTitle(GroupAssignation.ElementType.TITLE);
                    return element;
                })
                .collect(Collectors.toList());
    }
    
}
