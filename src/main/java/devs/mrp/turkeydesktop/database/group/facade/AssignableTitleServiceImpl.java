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

/**
 *
 * @author miguel
 */
public class AssignableTitleServiceImpl extends AssignableAbstractService implements AssignableElementService<Title.Type> {
    
    private final TitleService titleService = TitleServiceFactory.getService();
    
    @Override
    public List<AssignableElement<Title.Type>> positiveElementsWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.TITLE),
                Title.Type.POSITIVE);
    }

    @Override
    public List<AssignableElement<Title.Type>> negativeElementsWithAssignation() {
        return elementsWithAssignation(getAssignationsMap(GroupAssignation.ElementType.TITLE),
                Title.Type.NEGATIVE);
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
