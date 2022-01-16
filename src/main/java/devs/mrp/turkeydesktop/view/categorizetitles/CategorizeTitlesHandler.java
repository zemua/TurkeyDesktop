/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.database.titledlog.FTitledLogServiceFacade;
import devs.mrp.turkeydesktop.database.titledlog.ITitledLogServiceFacade;
import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizetitles.element.CategorizeTitlesElement;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.FTitleConditionsPanel;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class CategorizeTitlesHandler extends PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> {
    
    ITitledLogServiceFacade facadeService = FTitledLogServiceFacade.getService();
    
    public CategorizeTitlesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> initPanel() {
        return FCategorizeTitlesPanel.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> pan) {
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
        attachItemsToListPanel(new Date(), new Date());
    }
    
    private void attachItemsToListPanel(Date from, Date to) {
        JPanel panel = (JPanel)this.getPanel().getProperty(CategorizeTitlesEnum.LIST_PANEL);
        if (panel == null) {return;}
        panel.removeAll(); // clear in case it has been filled before
        List<TitledLog> titledLogs = facadeService.getLogsDependablesWithTitleConditions(from, to);
        titledLogs.sort((c1,c2) -> Long.valueOf(c2.getElapsed()).compareTo(c1.getElapsed()));
        titledLogs.forEach(t -> {
            CategorizeTitlesElement element = new CategorizeTitlesElement(t.getTitle(), t.getQtyPositives(), t.getQtyNegatives());
            element.setTitledLog(t);
            panel.add(element);
            setTagClickListener(element, t);
        });
    }
    
    private void setTagClickListener(Feedbacker<JLabel, String> label, TitledLog titledLog) {
        label.addFeedbackListener((tipo, feedback) -> {
            var handler = FTitleConditionsPanel.getHandler(getFrame(), CategorizeTitlesHandler.this, titledLog);
            handler.show();
        });
    }
    
}
