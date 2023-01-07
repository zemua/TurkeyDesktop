package devs.mrp.turkeydesktop.database.group.assignations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupAssignation {
    
    public static final String TYPE = "TYPE";
    public static final String ELEMENT_ID = "ELEMENT_ID";
    public static final String GROUP_ID = "GROUP_ID";
    
    private ElementType type;
    private String elementId;
    private long groupId;
    
    public enum ElementType {
        PROCESS, TITLE;
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    public boolean equals(GroupAssignation element) {
        return element != null && (
                (this.getElementId() == null && element.getElementId() == null) || (this.getElementId().equals(element.getElementId())) &&
                (this.getGroupId() == element.getGroupId()) && 
                (this.getType() == null && element.getType() == null) || (this.getType().equals(element.getType())));
    }
    
}
