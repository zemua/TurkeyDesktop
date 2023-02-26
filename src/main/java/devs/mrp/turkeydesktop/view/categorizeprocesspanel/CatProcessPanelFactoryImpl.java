package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import devs.mrp.turkeydesktop.database.type.TypeFactoryImpl;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public class CatProcessPanelFactoryImpl implements CatProcessPanelFactory {
    
    private static CatProcessPanelFactoryImpl instance;
    
    private CatProcessPanelFactoryImpl() {}
    
    public static CatProcessPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new CatProcessPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> getPanel() {
        return new CatProcessPanel();
    }

    @Override
    public PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>, CatProcessPanelFactory> getHandler(PanelHandlerData<?> data) {
        return new CatProcessHandler(data, this);
    }

    @Override
    public LogAndTypeFacadeService getLogAndTypeFacadeService() {
        return LogAndTypeFacadeFactoryImpl.getInstance().getService();
    }

    @Override
    public TypeService getTypeService() {
        return TypeFactoryImpl.getInstance().getService();
    }
}
