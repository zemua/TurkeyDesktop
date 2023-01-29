package devs.mrp.turkeydesktop.database.group.expor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ExportedGroupId {
    private long group;
    private String file;
}
