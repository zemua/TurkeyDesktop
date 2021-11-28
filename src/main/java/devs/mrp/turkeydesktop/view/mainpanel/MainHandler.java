/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.FeedbackerPanel;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.times.TimesHandler;
import devs.mrp.turkeydesktop.view.times.TimesPanel;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class MainHandler extends PanelHandler<MainPanel.Types, AWTEvent> {
    
    private static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    private static final String TURKEY_IMG = "turkey.png";

    public MainHandler(JFrame frame, PanelHandler<?,?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanel<MainPanel.Types, AWTEvent> initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        this.getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(TURKEY_IMG));
        this.setPanel(FMainPanel.getMainPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanel<MainPanel.Types, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case CATEGORIZE:
                    // TODO
                    break;
                case TIMES:
                    initTimesHandler();
                    break;
                default:
                    break;
            }
        });
    }
    
    @Override
    protected void doExtraBeforeShow() {
        // Nothing to do here
    }
    
    private void initTimesHandler() {
        PanelHandler<TimesPanel.Types, AWTEvent> handler = new TimesHandler(this.getFrame(), this);
        handler.show();
    }
    
}
