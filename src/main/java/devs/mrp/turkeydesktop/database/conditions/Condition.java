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
    
    public static final String TARGET_ID = "TARGET_ID";
    
    private long targetId;
    
    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    @Override
    protected boolean otherFieldsEquals(Condition condition) {
        return condition.getTargetId() == this.targetId;
    }
    
}
