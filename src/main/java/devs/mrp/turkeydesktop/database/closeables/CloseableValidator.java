package devs.mrp.turkeydesktop.database.closeables;

public class CloseableValidator {
    
    public static boolean isValidKey(String elementName) {
        return elementName != null && !elementName.isEmpty();
    }
    
    public static boolean isInvalidKey(String elementName) {
        return !isValidKey(elementName);
    }
}
