package devs.mrp.turkeydesktop.database.type;

public class TypeValidator {
    
    public static boolean isValidKey(String rowId) {
        return rowId != null && !rowId.isEmpty();
    }
    
    public static boolean isInvalid(Type type) {
        return type == null || type.getProcess() == null || type.getProcess().isBlank() || type.getType() == null;
    }
    
}
