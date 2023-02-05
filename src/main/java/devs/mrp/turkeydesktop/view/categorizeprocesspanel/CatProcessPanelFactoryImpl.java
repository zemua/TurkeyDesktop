package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public class CatProcessPanelFactoryImpl implements CatProcessPanelFactory {
    
    private FactoryInitializer factory;
    
    public CatProcessPanelFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> getPanel() {
        return new CatProcessPanel();
    }

    @Override
    public PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> getHandler(PanelHandlerData<?> data) {
        return new CatProcessHandler(data, this);
    }

    @Override
    public LogAndTypeFacadeService getLogAndTypeFacadeService() {
        return factory.getLogAndTypeFacadeFactory().getService();
    }
}
