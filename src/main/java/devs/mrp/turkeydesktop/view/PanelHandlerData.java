package devs.mrp.turkeydesktop.view;

import javax.swing.JFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PanelHandlerData<T> {
    private JFrame frame;
    private PanelHandler<?, ?, ?> caller;
    private T entity;
}
