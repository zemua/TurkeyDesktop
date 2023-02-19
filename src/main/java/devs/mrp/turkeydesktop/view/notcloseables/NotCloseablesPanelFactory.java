package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import javax.swing.JFrame;

public interface NotCloseablesPanelFactory {
    
    FeedbackerPanelWithFetcher<NotCloseablesEnum,Object> getPanel();
    PanelHandler<NotCloseablesEnum,Object,FeedbackerPanelWithFetcher<NotCloseablesEnum,Object>, NotCloseablesPanelFactory> getHandler(JFrame frame, PanelHandler<?,?,?,?> caller);
    TypeService getTypeService();
    CloseableService getCloseableService();
    ConfirmationWithDelay getPopupMaker();
    
}
