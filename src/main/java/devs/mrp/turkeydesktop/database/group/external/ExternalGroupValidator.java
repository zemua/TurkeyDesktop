package devs.mrp.turkeydesktop.database.group.external;

public class ExternalGroupValidator {
    
    public static boolean isValidKey(Long rowId) {
        return rowId != null && rowId > 0;
    }
    
    public static boolean isInvalid(ExternalGroup externalGroup) {
        return externalGroup == null || externalGroup.getFile() == null || externalGroup.getFile().isBlank() || externalGroup.getFile().length() > 500 || externalGroup.getGroup() < 1;
    }
    
}
