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
public class Condition {
    
    public static final String ID = "ID";
    public static final String CONDITION_TYPE = "CONDITION_TYPE";
    public static final String TARGET_ID = "TARGET_ID";
    public static final String USAGE_TIME_CONDITION = "USAGE_TIME_CONDITION";
    public static final String LAST_DAYS_CONDITION = "LAST_DAYS_CONDITION";
    
    public enum ConditionType {
        ANOTHER_GROUP, EXT_FILE, RAND_CHECK;
    }
    
    private long id;
    private ConditionType conditionType;
    private long targetId;
    private long usageTimeCondition;
    private long lastDaysCondition;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getUsageTimeCondition() {
        return usageTimeCondition;
    }

    public void setUsageTimeCondition(long usageTimeCondition) {
        this.usageTimeCondition = usageTimeCondition;
    }

    public long getLastDaysCondition() {
        return lastDaysCondition;
    }

    public void setLastDaysCondition(long lastDaysCondition) {
        this.lastDaysCondition = lastDaysCondition;
    }
    
    public boolean equals(Condition condition) {
        if (condition == null) {
            return false;
        }
        if (this.conditionType == null) {
            if (condition.getConditionType() != null) {
                return false;
            }
        } else {
            if (!this.conditionType.equals(condition.getConditionType())) {
                return false;
            }
        }
        
    }
    
}
