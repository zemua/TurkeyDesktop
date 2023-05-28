package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;

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
