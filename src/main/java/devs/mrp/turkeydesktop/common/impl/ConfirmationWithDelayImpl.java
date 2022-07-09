/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
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
    
    private static final int DEFAULT_WAITING_SECONDS = 30;
    
    private Popup popup;
    private JFrame frame;
    private JPanel panel;
    private PopupFactory factory;
    private Runnable runnablePositive;
    private Runnable runnableNegative;
    private String confirmValue;
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    
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
        frame.setEnabled(false);
        countdown(acceptButton, secondsDelay, confirm);
    }
    
    @Override
    public void show(JFrame parent, Runnable runnablePositive, Runnable runnableNegative) {
        this.show(parent,
                localeMessages.getString("areYouSureYouShouldDoThis"),
                localeMessages.getString("cancel"),
                localeMessages.getString("confirm"),
                runnablePositive,
                runnableNegative,
                DEFAULT_WAITING_SECONDS);
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
        frame.setEnabled(true);
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
        panel.setBackground(Color.pink);
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(cancelButton, BorderLayout.WEST);
        panel.add(acceptButton, BorderLayout.EAST);
        
        Point location = frame.getLocation();
        popup = factory.getPopup(frame, panel, location.x, location.y);
        return acceptButton;
    }
    
}
