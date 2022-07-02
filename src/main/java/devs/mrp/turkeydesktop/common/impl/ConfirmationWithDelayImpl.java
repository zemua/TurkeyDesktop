/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ConfirmationWithDelayImpl extends JFrame implements ActionListener, ConfirmationWithDelay {
    
    private Popup popup;
    JFrame frame;
    JPanel panel;
    PopupFactory factory;

    @Override
    public void show(String title, String msg, String cancel, String confirm, Runnable runnable) {
        frame = new JFrame("popup");
        factory = new PopupFactory();
        
        JLabel label = new JLabel(msg);
        JButton cancelButton = new JButton(cancel);
        cancelButton.addActionListener(this);
        JButton acceptButton = new JButton(confirm);
        acceptButton.addActionListener(this);
        
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(cancelButton, BorderLayout.WEST);
        panel.add(acceptButton, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
