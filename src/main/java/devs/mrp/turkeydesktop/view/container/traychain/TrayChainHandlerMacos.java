package devs.mrp.turkeydesktop.view.container.traychain;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * there is an error in mac if using dorkbox system tray
 * so we are making use of the java awt default implementation
 */
@Slf4j
public class TrayChainHandlerMacos extends TrayChainBaseHandler {
    
    private final TrayChainFactory factory;
    private final TimeConverter timeConverter;
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private ResourceHandler<Image,ImagesEnum> imageHandler = ResourceHandlerFactory.getImagesHandler();
    private TrayIcon trayIcon;
    private MenuItem timeItem;
    
    TrayChainHandlerMacos(TrayChainFactory factory) {
        this.factory = factory;
        this.timeConverter = factory.getTimeConverter();
    }

    @Override
    protected boolean canHandle(String tipo) {
        log.debug("checking if can handle {} for mac", tipo);
        return Platform.isMac();
    }

    @Override
    protected void handle(JFrame frame) {
        log.debug("handling for mac");
        trayIcon = null;
        if (!SystemTray.isSupported()) {
            throw new RuntimeException("Unable to load SystemTray!");
        }
        SystemTray tray = SystemTray.getSystemTray();
        Image image = imageHandler.getResource(ImagesEnum.TURKEY);
        
        PopupMenu popup = new PopupMenu();
        
        MenuItem openItem = new MenuItem(localeMessages.getString("open"));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
                // because frame.toFront() doesn't work
                frame.setAlwaysOnTop(true);
                frame.setAlwaysOnTop(false);
                factory.getWatchDog().begin(); // way to start watchdog if it gets stuck
            }
        });
        popup.add(openItem);
        
        MenuItem hideItem = new MenuItem(localeMessages.getString("hide"));
        hideItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.setExtendedState(JFrame.ICONIFIED);
            }
        });
        popup.add(hideItem);
        
        timeItem = new MenuItem("00:00");
        timeItem.setEnabled(false);
        popup.add(timeItem);
        
        trayIcon = new TrayIcon(image, "Turkey Desktop", popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e);
        }

    }

    @Override
    protected void setTrayIcon(Image image) {
        if (trayIcon != null) {
            trayIcon.setImage(image);
        }
    }

    @Override
    protected void setTimeLeft(long millis) {
        if (timeItem != null) {
            timeItem.setLabel(timeConverter.millisToHM(millis));
        }
    }

}
