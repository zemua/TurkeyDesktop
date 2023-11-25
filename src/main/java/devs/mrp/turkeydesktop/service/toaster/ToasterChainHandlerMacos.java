package devs.mrp.turkeydesktop.service.toaster;

import com.sun.jna.Platform;
import devs.mrp.turkeydesktop.common.ChainHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToasterChainHandlerMacos extends ChainHandler<String> {

    private Runtime r = Runtime.getRuntime();
    private String title = "Time Turkey Desktop";
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isMac();
    }

    @Override
    protected void handle(String data) {
        String message = String.format("display notification \"%s\" with title \"%s\"", data, title);
        String[] sender = {"osascript", "-e", message};
        try {
            Process p = r.exec(sender);
        } catch (IOException ex) {
            Logger.getLogger(ToasterChainHandlerMacos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
