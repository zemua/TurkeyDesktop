package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class TimesPanelFactoryImpl implements TimesPanelFactory {
    
    private final FactoryInitializer factory;
    
    public TimesPanelFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> getPanel() {
        return new TimesPanel();
    }
    
    @Override
    public PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new TimesHandler(frame, caller, this);
    }

    @Override
    public TimeLogService getTimeLogService() {
        return factory.getTimeLogServiceFactory().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }
    
}
