/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

/**
 *
 * @author miguel
 */
public class Condition extends AbstractCondition<Condition> {
    
    public static final String GROUP_ID = "GROUP_ID";
    
    private long groupId;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    protected boolean otherFieldsEquals(Condition condition) {
        return condition.getGroupId() == this.groupId;
    }
    
}
