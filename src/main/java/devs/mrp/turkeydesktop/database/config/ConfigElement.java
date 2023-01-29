package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ConfigElement {
    
    public static final String KEY = "KEY";
    public static final String VALUE = "VALUE";
    public static final long MAX_LENGTH = 150;
    
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
