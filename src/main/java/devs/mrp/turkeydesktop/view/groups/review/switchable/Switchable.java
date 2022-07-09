/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review.switchable;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 * 
 * Long is the id of the element for recognition on feedback
 * Boolean indicates wether the checkbox has been activated or deactivated
 */
public class Switchable extends JPanel implements Feedbacker<String, Boolean> {
    
    private LocaleMessages locale = LocaleMessages.getInstance();
    private List<FeedbackListener<String, Boolean>> listeners = new ArrayList<>();
    
    private JLabel label = new JLabel();
    private JCheckBox check = new JCheckBox();
    
    public Switchable(String text, boolean checked, boolean enabled) {
        label.setText(text);
        check.setSelected(checked);
        check.setEnabled(enabled);
        initializeLayout();
        initializeListener();
    }
    
    @Override
    public void addFeedbackListener(FeedbackListener<String, Boolean> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(String id, Boolean activated) {
        listeners.forEach(l -> l.giveFeedback(id, activated));
    }
    
    private void initializeLayout() {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
        this.add(check);
    }
    
    private void initializeListener() {
        check.addActionListener((event) -> {
            giveFeedback(label.getText(), check.isSelected());
        });
    }
    
    public void setSelected(boolean selected) {
        this.check.setSelected(selected);
    }
    
}
