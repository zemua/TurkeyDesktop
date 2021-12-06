/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

/**
 *
 * @author miguel
 */
public class Type {
    
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String TYPE = "TYPE";
    
    public enum Types {
        UNDEFINED, POSITIVE, NEGATIVE, NEUTRAL, DEPENDS;
    }
    
}
