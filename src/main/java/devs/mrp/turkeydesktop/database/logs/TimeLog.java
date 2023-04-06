package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TimeLog {
    
    public static final String ID = "ID";
    public static final String EPOCH = "EPOCH";
    public static final String ELAPSED = "ELAPSED";
    public static final String COUNTED = "COUNTED";
    public static final String ACCUMULATED = "ACCUMULATED";
    public static final String PID = "PID";
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String WINDOW_TITLE = "WINDOW_TITLE";
    public static final String IDLE = "IDLE";
    
    @Setter
    @Getter
    private long id;
    @Setter
    @Getter
    private long epoch;
    @Setter
    @Getter
    private long elapsed;
    @Setter
    @Getter
    private boolean idle;
    @Setter
    @Getter
    private long counted;
    @Setter
    @Getter
    private long accumulated;
    @Setter
    @Getter
    private String pid;
    @Setter
    @Getter
    private String processName;
    @Setter
    @Getter
    private String windowTitle;
    @Setter
    @Getter
    private long groupId;
    @Setter
    @Getter
    private Type.Types type;
    @Setter
    @Getter
    private boolean blockable;
    
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    
}
