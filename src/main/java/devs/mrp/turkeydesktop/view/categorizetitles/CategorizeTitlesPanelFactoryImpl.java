package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogServiceFacade;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.TitleConditionsEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.TitleConditionsPanelFactory;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.TitleConditionsPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class CategorizeTitlesPanelFactoryImpl implements CategorizeTitlesPanelFactory {
    
    private static CategorizeTitlesPanelFactoryImpl instance;
    
    private CategorizeTitlesPanelFactoryImpl() {}
    
    public static CategorizeTitlesPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new CategorizeTitlesPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> getPanel(){
        return new CategorizeTitlesPanel();
    }
    
    @Override
    public PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>, CategorizeTitlesPanelFactory> getHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller) {
        return new CategorizeTitlesHandler(frame, caller, this);
    }

    @Override
    public TitledLogServiceFacade getTitledLogServiceFacade() {
        return TitledLogFacadeFactoryImpl.getInstance().getService();
    }

    @Override
    public PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>, TitleConditionsPanelFactory> getHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller, TitledLog titledLog) {
        return TitleConditionsPanelFactoryImpl.getInstance().getHandler(frame, caller, titledLog);
    }
    
}
