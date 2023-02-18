package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import devs.mrp.turkeydesktop.view.mainpanel.MainEnum;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class StarterFactoryImpl implements StarterFactory {
    
    private FactoryInitializer factory;
    
    public StarterFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public Starter getStarterController() {
        return new StarterImpl(this);
    }

    @Override
    public WatchDog getWatchDog() {
        return factory.getWatchDogFactory().getInstance();
    }

    @Override
    public PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> getMainHandler(JFrame frame) {
        return factory.getMainPanelFactory().getMainHandler(frame);
    }

    @Override
    public JFrame getMainContainer() {
        return factory.getMainContainerFactory().getContainer();
    }
}
