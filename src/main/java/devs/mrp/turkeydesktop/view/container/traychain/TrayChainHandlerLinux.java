/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.view.container.traychain;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.mainpanel.MainHandler;
import dorkbox.systemTray.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 *
 * @author ncm55070
 */
public class TrayChainHandlerLinux extends ChainHandler<JFrame> {
    
    private LocaleMessages localeMessages = LocaleMessages.getInstance();

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(JFrame frame) {
        SystemTray tray = SystemTray.get();
        if (tray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }
        
        tray.installShutdownHook();
        tray.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(MainHandler.TURKEY_IMG)));
        tray.setStatus("Running");
        
        JMenuItem openItem = new JMenuItem(localeMessages.getString("open"));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
            }
        });
        JMenuItem hideItem = new JMenuItem(localeMessages.getString("hide"));
        hideItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.setExtendedState(JFrame.ICONIFIED);
            }
        });
        tray.getMenu().add(openItem);
        tray.getMenu().add(hideItem);
    }
    
}
