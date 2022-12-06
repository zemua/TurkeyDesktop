/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author miguel
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Type {
    
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String TYPE = "TYPE";

    private String process;
    private Types type;

    public enum Types {
        UNDEFINED, POSITIVE, NEGATIVE, NEUTRAL, DEPENDS;
    }
}
