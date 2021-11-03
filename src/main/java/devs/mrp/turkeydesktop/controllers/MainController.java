/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.controllers;

import devs.mrp.turkeydesktop.view.container.FContainer;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class MainController implements IStarter {
    
    private static final String MAIN_TITLE = "Time Turkey";
    private static final String TURKEY_IMG = "/devs/mrp/turkeydesktop/resources/img/turkey.png";

    private JFrame mainFrame;
    
    @Override
    public void start() {
        mainFrame = FContainer.getContainer();
        mainFrame.setTitle(MAIN_TITLE);
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainController.this.getClass().getResource(TURKEY_IMG)));
    }
    
}
