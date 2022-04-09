/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.container;

import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class MainContainerFactory {
    public static JFrame getContainer() {
        return new MainContainer();
    }
}
