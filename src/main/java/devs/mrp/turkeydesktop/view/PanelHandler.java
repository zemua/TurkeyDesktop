package devs.mrp.turkeydesktop.view;

import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.util.Objects;
import java.util.Optional;
import javax.swing.JFrame;

public abstract class PanelHandler<T, E, P extends FeedbackerPanelWithFetcher<T,E>> {
    
    private PanelHandler<?, ?, ?> caller;
    private P panel;
    private JFrame frame;
    private boolean listening = false;
    
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
        panel.updateUI();
        listening = true;
    }
    
    protected boolean isListening() {
        return listening;
    }
    
    protected abstract P initPanel();
    
    protected abstract void initListeners(P pan);
    
    protected abstract void doExtraBeforeShow();
    
    protected abstract void doBeforeExit();
    
    protected void exit() {
        listening = false;
        doBeforeExit();
        if (Objects.nonNull(this.getCaller())) {
            this.getCaller().show();
        }
    }

    private PanelHandler<?,?,?> getCaller() {
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
