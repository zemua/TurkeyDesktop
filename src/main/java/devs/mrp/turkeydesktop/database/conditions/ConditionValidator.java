package devs.mrp.turkeydesktop.database.conditions;

public class ConditionValidator {
    
    public static boolean isValidKey(Long rowId) {
        return rowId != null && rowId > 0;
    }
    
    public static boolean isInvalid(Long rowId) {
        return !isValidKey(rowId);
    }
    
    public static boolean isInvalidCondition(Condition condition) {
        return condition == null || condition.getGroupId() < 1 || condition.getTargetId() < 1;
    }
    
}
