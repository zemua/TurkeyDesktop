package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public interface TimesPanelFactory {
    
    FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> getPanel();
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller);
    TimeLogService getTimeLogService();
    TimeConverter getTimeConverter();
    
}
