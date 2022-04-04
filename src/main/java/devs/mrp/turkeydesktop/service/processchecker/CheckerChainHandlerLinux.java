/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import devs.mrp.turkeydesktop.common.ChainHandler;
import io.github.kingpulse.structs.xdo_t;
import io.github.kingpulse.xdotool;


/**
 *
 * @author miguel
 */
public class CheckerChainHandlerLinux extends ChainHandler<ProcessInfo> {
    
    private static final String DISPLAY_STRING = ":1";
    
    final X11 x11 = X11.INSTANCE;
    final XLib xlib = XLib.INSTANCE;
    private xdotool lib = xdotool.loadLib();
    private xdo_t xdo = null;
    
    public CheckerChainHandlerLinux() {
        xdo = lib.xdo_new(DISPLAY_STRING);
    }
    
    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }
    
    @Override
    protected void handle(ProcessInfo processInfo) {
        // Elements to be freed afterwards
        X11.Display display = x11.XOpenDisplay(null);
        X11.WindowByReference windowRef = new X11.WindowByReference();
        IntByReference focusRevertToReturn = new IntByReference();
        // ###############################
        X11.WindowByReference windowRootRef = new X11.WindowByReference();
        X11.WindowByReference parentWindowRef = new X11.WindowByReference();
        PointerByReference childrenRef = new PointerByReference();
        IntByReference childCountRef = new IntByReference();
        X11.XTextProperty parentname = new X11.XTextProperty();
        // ###############################
        X11.XWindowAttributes windowAttributes = new X11.XWindowAttributes();
        // ###############################
        
        // Get a reference to the window that is in focus
        xlib.XGetInputFocus(display, windowRef, focusRevertToReturn);
        // Get the hierarchy of the nodes to retrieve the parent
        x11.XQueryTree(display, windowRef.getValue(), windowRootRef, parentWindowRef, childrenRef, childCountRef);
        // Get the name of the parent window which is the title we are looking for
        x11.XGetWMName(display, parentWindowRef.getValue(), parentname);
        
        processInfo.setWindowTitle(parentname.value);
        
        int processPid = lib.xdo_get_pid_window(xdo, windowRef.getValue());
        
        x11.XGetWindowAttributes(display, windowRef.getValue(), windowAttributes);
        
        processInfo.setProcessPid(String.valueOf(processPid));
        
        // Free Memory
        x11.XFree(focusRevertToReturn.getPointer());
        x11.XFree(windowRef.getPointer());
        x11.XFree(display.getPointer());
        // ---
        x11.XFree(windowRootRef.getPointer());
        x11.XFree(parentWindowRef.getPointer());
        x11.XFree(childrenRef.getPointer());
        x11.XFree(childCountRef.getPointer());
        x11.XFree(parentname.getPointer());
    }
    
    public interface XLib extends X11 {
        
        XLib INSTANCE = (XLib) Native.loadLibrary("X11", XLib.class);
        
        //int XGetInputFocus(X11.Display display, X11.Window focus_return, Pointer revert_to_return);
        int XGetInputFocus(X11.Display display, X11.WindowByReference focus_return, IntByReference revert_to_return);
    }

}
