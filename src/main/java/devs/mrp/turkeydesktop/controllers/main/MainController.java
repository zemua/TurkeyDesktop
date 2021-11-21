/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.FWatchDog;
import devs.mrp.turkeydesktop.service.watchdog.IWatchDog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.FContainer;
import devs.mrp.turkeydesktop.view.mainpanel.FMainPanel;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class MainController implements IStarter {

    private JFrame mainFrame;
    private IWatchDog watchDog;
    private PanelHandler handler;
    
    @Override
    public void start() {
        initMainFrame();
        watchDog = FWatchDog.getInstance();
        watchDog.begin();
    }
    
    private void initMainFrame() {
        mainFrame = FContainer.getContainer();
        handler = FMainPanel.getMainHandler(mainFrame);
        handler.show();
    }
    
}
