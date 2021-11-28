/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.Feedbacker;
import devs.mrp.turkeydesktop.view.FeedbackerPanel;
import java.awt.AWTEvent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public abstract class FeedbackerPanelWithLogger<T, E> extends FeedbackerPanel<T, E> {
    
    public abstract JTextArea getLogger();
    
}
