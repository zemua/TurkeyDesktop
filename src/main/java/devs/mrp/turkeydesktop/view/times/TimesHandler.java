/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.view.FeedbackerPanel;
import devs.mrp.turkeydesktop.view.PanelHandler;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class TimesHandler extends PanelHandler<TimesPanel.Types, AWTEvent> {

    public TimesHandler(JFrame frame, PanelHandler<?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanel<TimesPanel.Types, AWTEvent> initPanel() {
        this.setPanel(new TimesPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanel<TimesPanel.Types, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    initCaller();
                    break;
                default:
                    break;
            }
        });
    }
    
    @Override
    protected void doExtraBeforeShow() {
        // TODO load db data
    }
    
    private void initCaller() {
        this.getCaller().show();
    }
    
}
