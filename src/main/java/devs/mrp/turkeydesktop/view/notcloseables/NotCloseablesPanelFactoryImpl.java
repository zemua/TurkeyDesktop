package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import javax.swing.JFrame;

public class NotCloseablesPanelFactoryImpl implements NotCloseablesPanelFactory {
    
    private FactoryInitializer factory;
    
    public NotCloseablesPanelFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<NotCloseablesEnum,Object> getPanel() {
        return new NotCloseablesPanel();
    }
    
    @Override
    public PanelHandler<NotCloseablesEnum,Object,FeedbackerPanelWithFetcher<NotCloseablesEnum,Object>> getHandler(JFrame frame, PanelHandler<?,?,?> caller) {
        return new NotCloseablesHandler(frame, caller, this);
    }

    @Override
    public TypeService getTypeService() {
        return factory.getTypeFactory().getService();
    }

    @Override
    public CloseableService getCloseableService() {
        return factory.getCloseableFactory().getService();
    }

    @Override
    public ConfirmationWithDelay getPopupMaker() {
        return new ConfirmationWithDelayFactory();
    }
    
}
