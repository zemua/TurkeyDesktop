package devs.mrp.turkeydesktop.database.imports;

public class ImportValidator {
    
    public static final int PATH_MAX_SIZE = 500;
    
    public static boolean isValidKey(String importPath) {
        return importPath != null && !importPath.isEmpty() && importPath.length() <= PATH_MAX_SIZE;
    }
    
    public static boolean isInvalid(String importPath) {
        return !isValidKey(importPath);
    }
    
}
