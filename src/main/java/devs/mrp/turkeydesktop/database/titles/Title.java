/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

/**
 *
 * @author miguel
 */
public class Title {
    
    public static final String SUB_STR = "SUB_STR";
    public static final String TYPE = "TYPE";
    
    private String subStr;
    private Title.Type type;
    
    public enum Type {
        POSITIVE, NEGATIVE;
    }

    public String getSubStr() {
        return subStr;
    }

    public void setSubStr(String subStr) {
        this.subStr = subStr;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
}
