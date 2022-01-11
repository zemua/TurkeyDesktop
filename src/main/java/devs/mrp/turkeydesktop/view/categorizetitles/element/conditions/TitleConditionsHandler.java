/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author miguel
 */
public class TitleConditionsHandler extends PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>> {

    private TitledLog mTitledLog;
    
    public TitleConditionsHandler(JFrame frame, PanelHandler<?, ?, ?> caller, TitledLog titledLog) {
        super(frame, caller);
        mTitledLog = titledLog;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> initPanel() {
        return FTitleConditionsPanel.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    this.getCaller().show();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        fillFields();
        fillConditions();
    }
    
    private void fillFields() {
        JTextField title = (JTextField)getPanel().getProperty(TitleConditionsEnum.TITLE);
        title.setText(mTitledLog.getTitle());
    }
    
    private void fillConditions() {
        // TODO
    }
    
}
