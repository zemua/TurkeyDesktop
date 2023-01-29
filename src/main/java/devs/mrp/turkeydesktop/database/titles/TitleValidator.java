/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.database.titles;

/**
 *
 * @author ncm55070
 */
public class TitleValidator {
    
    public static boolean isValidKey(String titleSubString) {
        return titleSubString != null && !titleSubString.isEmpty();
    }
    
    public static boolean isInvalid(Title element) {
        return element == null || !TitleValidator.isValidKey(element.getSubStr()) || element.getType() == null;
    }
    
}
