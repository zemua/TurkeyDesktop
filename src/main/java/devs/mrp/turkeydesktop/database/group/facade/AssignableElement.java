/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;

/**
 *
 * @author miguel
 */
public class AssignableElement<TYPE> {
    
    private String elementName;
    private GroupAssignation.ElementType processOrTitle;
    private TYPE positiveOrNegative;
    private Long groupId;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public GroupAssignation.ElementType getProcessOrTitle() {
        return processOrTitle;
    }

    public void setProcessOrTitle(GroupAssignation.ElementType processOrTitle) {
        this.processOrTitle = processOrTitle;
    }

    public TYPE getPositiveOrNegative() {
        return positiveOrNegative;
    }

    public void setPositiveOrNegative(TYPE positiveOrNegative) {
        this.positiveOrNegative = positiveOrNegative;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
}
