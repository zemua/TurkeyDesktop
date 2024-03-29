package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacade;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConditionElement extends RemovableLabel<GroupConditionFacade> {

    public ConditionElement(GroupConditionFacade element) {
        super(element);
    }
    
    @Override
    protected String getNameFromElement(GroupConditionFacade facade) {
        return facade.toString();
    }
    
    @Override
    protected void initializeLabel(GroupConditionFacade element) {
        element.stringify().subscribe(stringResult -> {
            label = new JLabel();
            label.setText(stringResult);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    showButton();
                }
            });
            alternativeInitializeButton();
            alternativeInitializePanel();
            this.revalidate();
        });
    }

    @Override
    protected void initializeOtherElements(GroupConditionFacade facade) {
    }

    @Override
    protected void addOtherItems(JPanel panel, GroupConditionFacade facade) {
    }
    
    @Override
    protected void initializeButton() {
        // remove parent implementation
    }
    
    protected void alternativeInitializeButton() {
        // to be called after label is loaded
        button = new JButton();
        button.setText(locale.getString("remove"));
        button.setEnabled(false);
        button.setVisible(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                giveFeedback(element, RemovableLabel.Action.DELETE);
                hideButton();
            }
        });
    }
    
    @Override
    protected void initializePanel() {
        // remove parent implementation
    }
    
    private void alternativeInitializePanel() {
        // to be called after label is loaded
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        addOtherItems(this, element);
        add(button);
    }
    
}
