package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactoryImpl;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.type.TypeFactoryImpl;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import javax.swing.JFrame;

public class NotCloseablesPanelFactoryImpl implements NotCloseablesPanelFactory {
    
    private static NotCloseablesPanelFactoryImpl instance;
    
    private NotCloseablesPanelFactoryImpl() {}
    
    public static NotCloseablesPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new NotCloseablesPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<NotCloseablesEnum,Object> getPanel() {
        return new NotCloseablesPanel();
    }
    
    @Override
    public PanelHandler<NotCloseablesEnum,Object,FeedbackerPanelWithFetcher<NotCloseablesEnum,Object>, NotCloseablesPanelFactory> getHandler(JFrame frame, PanelHandler<?,?,?,?> caller) {
        return new NotCloseablesHandler(frame, caller, this);
    }

    @Override
    public TypeService getTypeService() {
        return TypeFactoryImpl.getInstance().getService();
    }

    @Override
    public CloseableService getCloseableService() {
        return CloseableFactoryImpl.getInstance().getService();
    }

    @Override
    public ConfirmationWithDelay getPopupMaker() {
        return new ConfirmationWithDelayFactory();
    }
    
}
