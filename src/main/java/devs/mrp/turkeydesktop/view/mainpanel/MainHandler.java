/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.FCatProcessPanel;
import devs.mrp.turkeydesktop.view.times.FTimesPanel;
import devs.mrp.turkeydesktop.view.times.TimesEnum;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class MainHandler extends PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> {
    
    private static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    private static final String TURKEY_IMG = "turkey.png";
    
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> timesHandler;
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> categoryProcessHandler;

    public MainHandler(JFrame frame, PanelHandler<?,?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<MainEnum, AWTEvent> initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        this.getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(TURKEY_IMG));
        this.setPanel(FMainPanel.getMainPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<MainEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case CATEGORIZE:
                    initCategorizeHandler();
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
    
    private void initTimesHandler() { // TODO prevent creating new if not null
        if (timesHandler == null) {
            timesHandler = FTimesPanel.getHandler(this.getFrame(), this);
        }
        timesHandler.show();
    }
    
    private void initCategorizeHandler() { // TODO prevent creating new if not null
        if(categoryProcessHandler == null) {
            categoryProcessHandler = FCatProcessPanel.getHandler(this.getFrame(), this);
        }
        categoryProcessHandler.show();
    }
    
}
