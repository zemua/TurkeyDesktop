/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.groupcondition;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;

/**
 *
 * @author miguel
 */
public class GroupConditionFacade {
    
    private LocaleMessages locale = LocaleMessages.getInstance();
    
    private long conditionId;
    private long groupId;
    private String groupName;
    private long targetId;
    private String targetName;
    private long usageTimeCondition;
    private long lastDaysCondition;

    public long getConditionId() {
        return conditionId;
    }

    public void setConditionId(long conditionId) {
        this.conditionId = conditionId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(locale.getString("if"));
        builder.append(" ");
        builder.append(targetName);
        builder.append(" ");
        builder.append(locale.getString("hasUsed"));
        builder.append(" ");
        builder.append(TimeConverter.millisToHM(usageTimeCondition));
        builder.append(" ");
        if (TimeConverter.daysFromMillis(lastDaysCondition) != 0) {
            builder.append(locale.getString("inTheLast"));
            builder.append(" ");
            builder.append(TimeConverter.daysFromMillis(lastDaysCondition));
            builder.append(" ");
            builder.append(locale.getString("days"));
        } else {
            builder.append(locale.getString("today"));
        }
        return builder.toString();
    }
    
}