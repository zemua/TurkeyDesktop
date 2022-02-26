/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view;

import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 * 
 * @T type of the feedback, could be an enum usually
 * @E event, for swing usually AWTEvent
 */
public abstract class PanelHandler<T, E, P extends FeedbackerPanelWithFetcher<T,E>> {
    
    private PanelHandler<?, ?, ?> caller;
    private P panel;
    private JFrame frame;
    
    public PanelHandler(JFrame frame, PanelHandler<?,?,?> caller) {
        this.caller = caller;
        this.frame = frame;
        panel = initPanel();
        initListeners(panel);
    }
    
    public void show() {
        doExtraBeforeShow();
        frame.setContentPane(panel);
        frame.revalidate();
        //panel.revalidate();
        panel.updateUI();
    }
    
    protected abstract P initPanel();
    
    protected abstract void initListeners(P pan);
    
    protected abstract void doExtraBeforeShow();

    public PanelHandler<?,?,?> getCaller() {
        return caller;
    }

    public P getPanel() {
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setPanel(P panel) {
        this.panel = panel;
    }
    
    protected Optional<Object> getObjectFromPanel(T type, Class clazz) {
        Object retrieved = this.getPanel().getProperty(type);
        if (retrieved == null || !(clazz.isInstance(retrieved))) {
            return Optional.ofNullable(null);
        }
        return Optional.of(retrieved);
    }
    
}
