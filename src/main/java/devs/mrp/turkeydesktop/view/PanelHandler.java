/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public abstract class PanelHandler {
    
    private PanelHandler caller;
    private JPanel panel;
    private JFrame frame;
    
    public PanelHandler(JFrame frame, PanelHandler caller) {
        this.caller = caller;
        this.frame = frame;
        panel = initPanel();
        initListeners(panel);
    }
    
    public void show() {
        frame.setContentPane(panel);
        frame.revalidate();
    }
    
    protected abstract JPanel initPanel();
    
    protected abstract void initListeners(JPanel pan);

    public PanelHandler getCaller() {
        return caller;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
    
}
