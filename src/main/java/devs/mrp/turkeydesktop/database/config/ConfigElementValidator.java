package devs.mrp.turkeydesktop.database.config;

public class ConfigElementValidator {
    
    public static boolean isValidKey(String elementName) {
        return elementName != null && !elementName.isEmpty();
    }
    
}
