package devs.mrp.turkeydesktop.database.conditions;

import lombok.ToString;

@ToString
public class Condition extends AbstractCondition<Condition> {
    
    public static final String TARGET_ID = "TARGET_ID";
    
    static final int ID_POSITION = 1;
    
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
