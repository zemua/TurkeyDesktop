/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.common.Feedbacker;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/**
 *
 * @author zemua
 */
public class ConfirmationWithDelayImpl extends JFrame implements ActionListener, ConfirmationWithDelay, Feedbacker<Object, Boolean> {
    
    private List<FeedbackListener<Object, Boolean>> listeners = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(ConfirmationWithDelayImpl.class.getName());
    
    private Popup popup;
    private JFrame frame;
    private JPanel panel;
    private PopupFactory factory;
    private Runnable runnablePositive;
    private Runnable runnableNegative;
    
    private String OK = "OK";
    private String CANCEL = "CANCEL";

    @Override
    public void show(JFrame parent, String msg, String cancel, String confirm, Runnable runnablePositive, Runnable runnableNegative, int secondsDelay) {
        this.frame = parent;
        this.runnablePositive = runnablePositive;
        this.runnableNegative = runnableNegative;
        JButton acceptButton = createPopup(msg, cancel, confirm);
        popup.show();
        countdown(acceptButton, secondsDelay, confirm);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (action.equals(OK)) {
            popup.hide();
            runnablePositive.run();
            giveFeedback(null, true);
        }
        else {
            popup.hide();
            runnableNegative.run();
            giveFeedback(null, false);
        }
    }
    
    private void countdown(JButton button, int seconds, String btnText) {
        int remaining = seconds;
        button.setText(btnText + " (" + remaining + ")");
        while(remaining > 0 && button.isVisible()) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "interrupted exception while waiting to decrease delay on button", e);
            }
            remaining --;
            button.setText(btnText + " (" + remaining + ")");
        }
        button.setText(btnText);
        button.setEnabled(true);
    }
    
    private JButton createPopup(String msg, String cancel, String confirm) {
        this.runnablePositive = runnablePositive;
        factory = new PopupFactory();
        
        JLabel label = new JLabel(msg);
        JButton cancelButton = new JButton(CANCEL);
        cancelButton.setText(cancel);
        cancelButton.addActionListener(this);
        JButton acceptButton = new JButton(OK);
        acceptButton.setText(confirm);
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(this);
        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(cancelButton, BorderLayout.WEST);
        panel.add(acceptButton, BorderLayout.EAST);
        
        popup = factory.getPopup(frame, panel, 0, 0);
        return acceptButton;
    }

    @Override
    public void addFeedbackListener(FeedbackListener<Object, Boolean> listener) {
        listeners.add(listener);
    }

    @Override
    public void giveFeedback(Object tipo, Boolean feedback) {
        listeners.forEach(l -> l.giveFeedback(tipo, feedback));
    }
    
}
