package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.database.titledlog.TitledLog;
import devs.mrp.turkeydesktop.database.titles.TitleFactoryImpl;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class TitleConditionsPanelFactoryImpl implements TitleConditionsPanelFactory {
    
    private static TitleConditionsPanelFactoryImpl instance;
    
    private TitleConditionsPanelFactoryImpl() {}
    
    public static TitleConditionsPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TitleConditionsPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> getPanel() {
        return new TitleConditionsPanel();
    }
    
    @Override
    public PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>, TitleConditionsPanelFactory> getHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller, TitledLog titledLog) {
        return new TitleConditionsHandler(frame, caller, titledLog, this);
    }

    @Override
    public TitleService getTitleService() {
        return TitleFactoryImpl.getInstance().getService();
    }
    
}
