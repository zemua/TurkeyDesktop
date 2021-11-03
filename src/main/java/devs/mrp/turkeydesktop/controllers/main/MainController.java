/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.controllers.main;

import devs.mrp.turkeydesktop.view.container.FContainer;
import devs.mrp.turkeydesktop.view.mainpanel.FMainPanel;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class MainController implements IStarter {
    
    private static final String MAIN_TITLE = "Time Turkey";
    private static final String TURKEY_IMG = "resources/turkey.png";

    private JFrame mainFrame;
    
    @Override
    public void start() {
        initMainFrame();
    }
    
    private void initMainFrame() {
        mainFrame = FContainer.getContainer();
        mainFrame.setTitle(MAIN_TITLE);
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(TURKEY_IMG));
        mainFrame.setContentPane(initMainPanel());
        mainFrame.revalidate();
    }
    
    private JPanel initMainPanel() {
        JPanel panel = FMainPanel.getMainPanel();
        return panel;
    }
    
}
