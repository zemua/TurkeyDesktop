package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactoryImpl;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.MainContainerFactoryImpl;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import devs.mrp.turkeydesktop.view.mainpanel.MainEnum;
import devs.mrp.turkeydesktop.view.mainpanel.MainPanelFactory;
import devs.mrp.turkeydesktop.view.mainpanel.MainPanelFactoryImpl;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class StarterFactoryImpl implements StarterFactory {
    
    private static StarterFactoryImpl instance;
    
    private StarterFactoryImpl(){}
    
    public static StarterFactoryImpl getInstance() {
        if (instance == null) {
            instance = new StarterFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public Starter getStarterController() {
        return new StarterImpl(this);
    }

    @Override
    public WatchDog getWatchDog() {
        return WatchDogFactoryImpl.getInstance().getWatchDog();
    }

    @Override
    public PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>, MainPanelFactory> getMainHandler(JFrame frame) {
        return MainPanelFactoryImpl.getInstance().getMainHandler(frame);
    }

    @Override
    public JFrame getMainContainer() {
        return MainContainerFactoryImpl.getInstance().getContainer();
    }
}
