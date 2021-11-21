/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class MainHandler extends PanelHandler {
    
    private static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    private static final String TURKEY_IMG = "turkey.png";

    public MainHandler(JFrame frame, PanelHandler caller) {
        super(frame, caller);
    }
    
    @Override
    protected JPanel initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        this.getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(TURKEY_IMG));
        this.setPanel(FMainPanel.getMainPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(JPanel pan) {
        // TODO
    }
    
}
