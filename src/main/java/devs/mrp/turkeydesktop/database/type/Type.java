package devs.mrp.turkeydesktop.database.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Type {
    
    public static final int PROCESS_NAME_COLUMN = 1; 
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String TYPE = "TYPE";

    private String process;
    private Types type;

    public enum Types {
        UNDEFINED, POSITIVE, NEGATIVE, NEUTRAL, DEPENDS;
    }
}
