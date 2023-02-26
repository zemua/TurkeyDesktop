package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public interface TitleConditionsPanelFactory {
    
    FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> getPanel();
    PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>, TitleConditionsPanelFactory> getHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller, TitledLog titledLog);
    TitleService getTitleService();
    
}
