package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import javax.swing.JFrame;

public class StarterImpl implements Starter {

    private JFrame mainFrame;
    private WatchDog watchDog;
    private PanelHandler handler;
    private StarterFactory factory;
    
    public StarterImpl(StarterFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public void start() {
        initMainFrame();
        watchDog = factory.getWatchDog();
        watchDog.begin();
    }
    
    private void initMainFrame() {
        mainFrame = factory.getMainContainer();
        handler = factory.getMainHandler(mainFrame);
        handler.show();
    }
    
}
