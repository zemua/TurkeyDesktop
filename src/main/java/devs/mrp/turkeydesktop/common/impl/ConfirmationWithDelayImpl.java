/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingWorker;

/**
 *
 * @author zemua
 */
public class ConfirmationWithDelayImpl extends JFrame implements ActionListener, ConfirmationWithDelay {
    
    private static final Logger LOGGER = Logger.getLogger(ConfirmationWithDelayImpl.class.getName());
    
    private Popup popup;
    private JFrame frame;
    private JPanel panel;
    private PopupFactory factory;
    private Runnable runnablePositive;
    private Runnable runnableNegative;
    private String confirmValue;
    
    ConfirmationWithDelayImpl() {
        // just to restrict to the factory
    }

    @Override
    public void show(JFrame parent, String msg, String cancel, String confirm, Runnable runnablePositive, Runnable runnableNegative, int secondsDelay) {
        this.frame = parent;
        this.runnablePositive = runnablePositive;
        this.runnableNegative = runnableNegative;
        this.confirmValue = confirm;
        JButton acceptButton = createPopup(msg, cancel, confirm);
        popup.show();
        countdown(acceptButton, secondsDelay, confirm);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (action.equals(confirmValue)) {
            popup.hide();
            runnablePositive.run();
        }
        else {
            popup.hide();
            runnableNegative.run();
        }
    }
    
    private void countdown(JButton button, int seconds, String btnText) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
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
                return button;
            }
            
            @Override
            protected void done() {
                button.setText(btnText);
                button.setEnabled(true);
            }
        };
        worker.execute();
    }
    
    private JButton createPopup(String msg, String cancel, String confirm) {
        this.runnablePositive = runnablePositive;
        factory = new PopupFactory();
        
        JLabel label = new JLabel(msg);
        JButton cancelButton = new JButton(cancel);
        cancelButton.addActionListener(this);
        JButton acceptButton = new JButton(confirm);
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(this);
        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(cancelButton, BorderLayout.WEST);
        panel.add(acceptButton, BorderLayout.EAST);
        
        popup = factory.getPopup(frame, panel, 180, 100);
        return acceptButton;
    }
    
}
