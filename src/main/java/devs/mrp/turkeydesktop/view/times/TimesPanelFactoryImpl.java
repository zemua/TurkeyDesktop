package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class TimesPanelFactoryImpl implements TimesPanelFactory {
    
    private static TimesPanelFactoryImpl instance;
    
    private TimesPanelFactoryImpl() {}
    
    public static TimesPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TimesPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> getPanel() {
        return new TimesPanel();
    }
    
    @Override
    public PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>, TimesPanelFactory> getHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller) {
        return new TimesHandler(frame, caller, this);
    }

    @Override
    public TimeLogService getTimeLogService() {
        return TimeLogFactoryImpl.getInstance().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }
    
}
