/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactory;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.container.FContainer;
import devs.mrp.turkeydesktop.view.mainpanel.MainPanelFactory;
import java.awt.Dimension;
import javax.swing.JFrame;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;

/**
 *
 * @author miguel
 */
public class MainController implements IStarter {

    private JFrame mainFrame;
    private WatchDog watchDog;
    private PanelHandler handler;
    
    @Override
    public void start() {
        initMainFrame();
        watchDog = WatchDogFactory.getInstance();
        watchDog.begin();
    }
    
    private void initMainFrame() {
        mainFrame = FContainer.getContainer();
        //mainFrame.setSize(800, 250);
        //mainFrame.setResizable(false);
        handler = MainPanelFactory.getMainHandler(mainFrame);
        handler.show();
    }
    
}
