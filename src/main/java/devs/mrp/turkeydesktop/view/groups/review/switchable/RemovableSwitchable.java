package devs.mrp.turkeydesktop.view.groups.review.switchable;

import devs.mrp.turkeydesktop.common.RemovableLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class RemovableSwitchable extends RemovableLabel<Switchable> {
    
    public RemovableSwitchable(Switchable switchable) {
        super(switchable);
    }

    @Override
    protected String getNameFromElement(Switchable element) {
        return "";
    }

    @Override
    protected void initializeOtherElements(Switchable switchable) {
        // all initialized
    }

    @Override
    protected void addOtherItems(JPanel panel, Switchable switchable) {
        panel.add(switchable.getCheckBox());
    }
    
    @Override
    protected void initializeLabel(Switchable switchable) {
        label = switchable.getLabel();
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showButton();
            }
        });
    }
    
}
