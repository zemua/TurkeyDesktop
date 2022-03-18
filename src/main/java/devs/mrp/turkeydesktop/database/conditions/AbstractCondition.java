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
public abstract class AbstractCondition<T extends AbstractCondition> {
    
    public static final String ID = "ID";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String USAGE_TIME_CONDITION = "USAGE_TIME_CONDITION";
    public static final String LAST_DAYS_CONDITION = "LAST_DAYS_CONDITION";
    
    private long id;
    private long groupId;
    private long usageTimeCondition;
    private long lastDaysCondition;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    protected abstract boolean otherFieldsEquals(T condition);
    
    public boolean equals(T condition) {
        if (condition == null) {
            return false;
        }
        // check target id
        if (this.getGroupId() != condition.getGroupId()) {
            return false;
        }
        // check usage time
        if (this.getUsageTimeCondition() != condition.getUsageTimeCondition()) {
            return false;
        }
        // check last days condition
        if (this.getLastDaysCondition() != condition.getLastDaysCondition()) {
            return false;
        }
        // check fields made by inheritors
        return otherFieldsEquals(condition);
    }
    
}
