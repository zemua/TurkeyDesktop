package devs.mrp.turkeydesktop.view.container.traychain;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import dorkbox.systemTray.SystemTray;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 * new versions of gnome desktop have removed support of system tray and use a different api
 * so for ubuntu 18.04 onwards we need to make use of dorkbox implementation
 */
public class TrayChainHandlerLinux extends TrayChainBaseHandler {

    private final TrayChainFactory factory;
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private ResourceHandler<Image,ImagesEnum> imageHandler = ResourceHandlerFactory.getImagesHandler();
    private SystemTray tray;
    private JMenuItem timeItem;
    private final TimeConverter timeConverter;
    
    TrayChainHandlerLinux(TrayChainFactory factory) {
        this.factory = factory;
        timeConverter = factory.getTimeConverter();
    }

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(JFrame frame) {
        tray = SystemTray.get();
        if (tray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        tray.installShutdownHook();
        tray.setImage(imageHandler.getResource(ImagesEnum.TURKEY));
        tray.setStatus(localeMessages.getString("running"));

        JMenuItem openItem = new JMenuItem(localeMessages.getString("open"));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
                // because fram.toFront() doesnt work
                frame.setAlwaysOnTop(true);
                frame.setAlwaysOnTop(false);
                factory.getWatchDog().begin(); // way to start watchdog if it gets stuck
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
        timeItem = new JMenuItem("00:00");
        timeItem.setEnabled(false);
        tray.getMenu().add(openItem);
        tray.getMenu().add(hideItem);
        tray.getMenu().add(timeItem);
    }

    @Override
    protected void setTrayIcon(Image image) {
        if (tray != null) {
            tray.setImage(image);
        }
    }

    @Override
    protected void setTimeLeft(long millis) {
        tray.getMenu().remove(tray.getMenu().getLast());
        timeItem.setText(timeConverter.millisToHM(millis));
        tray.getMenu().add(timeItem);
    }

}
