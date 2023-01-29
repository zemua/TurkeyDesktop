package devs.mrp.turkeydesktop.database.group;

public class GroupValidator {
    
    public static boolean isValidKey(Long rowId) {
        return rowId != null && rowId > 0;
    }
    
    public static boolean isInvalid(Group group) {
        return group == null || group.getName() == null || group.getName().isBlank() || group.getType() == null;
    }
    
}
