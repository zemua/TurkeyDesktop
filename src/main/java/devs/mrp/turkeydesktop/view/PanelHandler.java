/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view;

import javax.swing.JFrame;

/**
 *
 * @author miguel
 * 
 * @T type of the feedback, could be an enum usually
 * @E event, for swing usually AWTEvent
 */
public abstract class PanelHandler<T, E> {
    
    private PanelHandler<?, ?> caller;
    private FeedbackerPanel<T, E> panel;
    private JFrame frame;
    
    public PanelHandler(JFrame frame, PanelHandler<?,?> caller) {
        this.caller = caller;
        this.frame = frame;
        panel = initPanel();
        initListeners(panel);
    }
    
    public void show() {
        doExtraBeforeShow();
        frame.setContentPane(panel);
        frame.revalidate();
    }
    
    protected abstract FeedbackerPanel<T, E> initPanel();
    
    protected abstract void initListeners(FeedbackerPanel<T, E> pan);
    
    protected abstract void doExtraBeforeShow();

    public PanelHandler<?,?> getCaller() {
        return caller;
    }

    public FeedbackerPanel<T, E> getPanel() {
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setPanel(FeedbackerPanel<T, E> panel) {
        this.panel = panel;
    }
    
}
