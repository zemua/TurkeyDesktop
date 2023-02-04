package devs.mrp.turkeydesktop.view;

import devs.mrp.turkeydesktop.database.group.Group;
import javax.swing.JFrame;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PanelHandlerData {
    private JFrame frame;
    private PanelHandler<?, ?, ?> caller;
    private Group group;
}
