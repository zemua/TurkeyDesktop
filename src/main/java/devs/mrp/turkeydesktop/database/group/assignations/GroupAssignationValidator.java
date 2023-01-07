package devs.mrp.turkeydesktop.database.group.assignations;

public class GroupAssignationValidator {
    
    public static boolean isValidKey(GroupAssignationDao.ElementId rowId) {
        return rowId.getElementId() != null && rowId.getType() != null && !rowId.getElementId().isEmpty();
    }
    
    public static boolean isInvalidAssignation(GroupAssignation assignation) {
        return assignation == null || assignation.getElementId() == null || assignation.getElementId().isBlank() || assignation.getGroupId() < 1 || assignation.getType() == null;
    }
    
}
