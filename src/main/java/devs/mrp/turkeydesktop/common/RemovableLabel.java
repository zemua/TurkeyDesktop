/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author miguel
 */
public abstract class RemovableLabel<ELEMENT> extends JPanel implements Feedbacker<ELEMENT, RemovableLabel.Action> {
    
    private LocaleMessages locale = LocaleMessages.getInstance();
    private List<FeedbackListener<ELEMENT, RemovableLabel.Action>> listeners = new ArrayList<>();
    
    protected ELEMENT element;
    protected JLabel label;
    private JButton button;
    
    public enum Action {
        DELETE;
    }
    
    public RemovableLabel(ELEMENT element) {
        this.element = element;
        initializeLabel();
        initializeButton();
        initializeOtherElements();
        initializePanel();
    }
    
    @Override
    public void addFeedbackListener(FeedbackListener<ELEMENT, RemovableLabel.Action> listener) {
        listeners.add(listener);
    }
    
    @Override
    public void giveFeedback(ELEMENT tipo, RemovableLabel.Action feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }
    
    protected abstract String getNameFromElement(ELEMENT element);
    
    protected void initializeLabel() {
        label = new JLabel();
        label.setText(getNameFromElement(element));
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showButton();
            }
        });
    }
    
    private void initializeButton() {
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
    
    protected void showButton() {
        button.setEnabled(true);
        button.setVisible(true);
        // hide back the button in 3 seconds
        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(3000);
                return null;
            }
            @Override
            protected void done() {
                hideButton();
            }
        };
        sw.execute();
    }
    
    public void hideButton() {
        if (button != null) {
            button.setEnabled(false);
            button.setVisible(false);
        }
    }
    
    protected abstract void initializeOtherElements();
    
    private void initializePanel() {
        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(label);
        addOtherItems(this);
        this.add(button);
    }
    
    protected abstract void addOtherItems(JPanel panel);
    
}
