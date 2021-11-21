/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.watchdog.FWatchDog;
import devs.mrp.turkeydesktop.service.watchdog.IWatchDog;
import devs.mrp.turkeydesktop.view.container.FContainer;
import devs.mrp.turkeydesktop.view.mainpanel.AMainPanel;
import devs.mrp.turkeydesktop.view.mainpanel.FMainPanel;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public class MainController implements IStarter {
    
    private static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    private static final String TURKEY_IMG = "turkey.png";

    private JFrame mainFrame;
    private AMainPanel mainPanel;
    private IWatchDog watchDog;
    private JPanel mPanel;
    
    @Override
    public void start() {
        initMainFrame();
        initWatchDog(mainPanel.getLogger());
    }
    
    private void initMainFrame() {
        mainFrame = FContainer.getContainer();
        mainFrame.setTitle(MAIN_TITLE);
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(TURKEY_IMG));
        initMainPanel();
        mainFrame.setContentPane(mainPanel);
        setMainListeners();
        mainFrame.revalidate();
    }
    
    private <T extends JPanel> void updatePane(T qPanel) {
        mPanel = qPanel;
        mainFrame.setContentPane(qPanel);
        mainFrame.revalidate();
    }
    
    private AMainPanel initMainPanel() {
        mainPanel = FMainPanel.getMainPanel();
        return mainPanel;
    }
    
    private IWatchDog initWatchDog(JTextArea logger) {
        watchDog = FWatchDog.getInstance();
        watchDog.begin(logger);
        return watchDog;
    }
    
    private void setMainListeners() {
        
    }
    
}
