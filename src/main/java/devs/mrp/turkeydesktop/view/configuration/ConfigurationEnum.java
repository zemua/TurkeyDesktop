/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

/**
 *
 * @author miguel
 */
public enum ConfigurationEnum {
    PROPORTION("4"), LOCKDOWN("false"), LOCKDOWN_FROM("0"), LOCKDOWN_TO("0"), LOCK_NOTIFY("true"), LOCK_NOTIFY_MINUTES("10"),
    MIN_LEFT_BUTTON("true"), MIN_LEFT_QTY("10"), EXPORT_TOGGLE("false"), EXPORT_PATH("");
    
    private final String def;
    
    ConfigurationEnum(String d) {
        this.def = d;
    }
    
    public String getDefault() {
        return this.def;
    }
}
