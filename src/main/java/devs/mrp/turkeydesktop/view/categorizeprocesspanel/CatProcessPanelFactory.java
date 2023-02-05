package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public interface CatProcessPanelFactory {
    
    FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> getPanel();
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> getHandler(PanelHandlerData<?> data);
    LogAndTypeFacadeService getLogAndTypeFacadeService();
    
}
