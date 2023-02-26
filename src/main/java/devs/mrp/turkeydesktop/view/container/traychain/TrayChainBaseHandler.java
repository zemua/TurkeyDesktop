package devs.mrp.turkeydesktop.view.container.traychain;

import devs.mrp.turkeydesktop.common.ChainHandler;
import java.awt.Image;
import javax.swing.JFrame;

public abstract class TrayChainBaseHandler extends ChainHandler<JFrame> {
    
    protected abstract void setTrayIcon(Image image);
    protected abstract void setTimeLeft(long millis);
    
    public void requestChangeIcon(String tipo, Image data) {
        if (!canHandle(tipo)) {
            if (mNextHandler != null && mNextHandler instanceof TrayChainBaseHandler) {
                ((TrayChainBaseHandler)mNextHandler).requestChangeIcon(tipo, data);
            }
            return;
        }
        setTrayIcon(data);
    }
    
    public void requestChangeTimeLeft(String tipo, long millis) {
        if (!canHandle(tipo)) {
            if (mNextHandler != null && mNextHandler instanceof TrayChainBaseHandler) {
                ((TrayChainBaseHandler)mNextHandler).requestChangeTimeLeft(tipo, millis);
            }
            return;
        }
        setTimeLeft(millis);
    }
    
}
