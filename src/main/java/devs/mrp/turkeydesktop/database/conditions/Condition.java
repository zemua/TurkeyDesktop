/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;

/**
 *
 * @author miguel
 */
public class Condition {
    
    public static final String ID = "ID";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String CONDITION_TYPE = "CONDITION_TYPE";
    public static final String TARGET_ID = "TARGET_ID";
    public static final String USAGE_TIME_CONDITION = "USAGE_TIME_CONDITION";
    public static final String LAST_DAYS_CONDITION = "LAST_DAYS_CONDITION";
    
    public enum ConditionType {
        ANOTHER_GROUP("anotherGroup"), EXT_FILE("externalFile"), RAND_CHECK("randomCheck");
        
        String localeName;
        LocaleMessages locale = LocaleMessages.getInstance();
        
        ConditionType(String name) {
            localeName = name;
        }
        
        public String getName() {
            return locale.getString(localeName);
        }
    }
    
    private long id;
    private long groupId;
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

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    public boolean equals(Condition condition) {
        if (condition == null) {
            return false;
        }
        // check condition type
        if (this.conditionType == null) {
            if (condition.getConditionType() != null) {
                return false;
            }
        } else {
            if (condition.getConditionType() == null || !this.conditionType.equals(condition.getConditionType())) {
                return false;
            }
        }
        // check target id
        if (this.getTargetId() != condition.getTargetId()) {
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
        // otherwise all are equal
        return true;
    }
    
}
