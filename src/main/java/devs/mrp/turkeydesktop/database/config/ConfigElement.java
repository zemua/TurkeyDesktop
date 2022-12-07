/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 *
 * @author miguel
 */
@NoArgsConstructor
@AllArgsConstructor
public class ConfigElement {
    
    public static final String KEY = "KEY";
    public static final String VALUE = "VALUE";
    
    private ConfigurationEnum key;
    private String value;

    public ConfigurationEnum getKey() {
        return key;
    }

    public void setKey(ConfigurationEnum key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
