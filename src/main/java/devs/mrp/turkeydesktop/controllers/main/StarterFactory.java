package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import devs.mrp.turkeydesktop.view.mainpanel.MainEnum;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public interface StarterFactory {
    
    Starter getStarterController();
    WatchDog getWatchDog();
    PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> getMainHandler(JFrame frame);
    
}
