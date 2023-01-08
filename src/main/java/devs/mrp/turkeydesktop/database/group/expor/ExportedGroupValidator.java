package devs.mrp.turkeydesktop.database.group.expor;

public class ExportedGroupValidator {
    
    public static boolean isValidKey(ExportedGroupId rowId) {
        return rowId.getFile() != null && !rowId.getFile().isEmpty() && rowId.getGroup() > 0;
    }
    
    public static boolean isInvalid(ExportedGroup exportedGroup) {
        return exportedGroup == null || exportedGroup.getFile() == null || exportedGroup.getFile().isBlank() || exportedGroup.getFile().length() > ExportedGroup.MAX_PATH_SIZE || exportedGroup.getGroup() < 1;
    }
    
}
