package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.database.titledlog.TitledLogServiceFacade;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public interface CategorizeTitlesPanelFactory {
    FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> getPanel();
    PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller);
    TitledLogServiceFacade getTitledLogServiceFacade();
}
