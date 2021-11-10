/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.processchecker;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.Dupla;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author miguel
 */
public class CheckerChainHandlerLinux extends ChainHandler<Dupla<String, String>> {

    /**
     * Referencias
     * https://stackoverflow.com/questions/5206633/find-out-what-application-window-is-in-focus-in-java
     * https://stackoverflow.com/questions/41503180/jna-ubuntu-xgetinputfocus
     * https://stackoverflow.com/questions/41804977/jna-jvm-fatal-error-xgetinputfocus-ubuntu
     */
    final X11 x11 = X11.INSTANCE;
    final XLib xlib = XLib.INSTANCE;

    public interface XLib extends X11 {

        XLib INSTANCE = (XLib) Native.loadLibrary("X11", XLib.class);

        //int XGetInputFocus(X11.Display display, X11.Window focus_return, Pointer revert_to_return);
        int XGetInputFocus(X11.Display display, X11.WindowByReference focus_return, IntByReference revert_to_return);
    }

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(Dupla<String, String> dupla) {
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
        
        // Get a reference to the window that is in focus
        xlib.XGetInputFocus(display, windowRef, focusRevertToReturn);
        // Get the hierarchy of the nodes to retrieve the parent
        x11.XQueryTree(display, windowRef.getValue(), windowRootRef, parentWindowRef, childrenRef, childCountRef);
        // Get the name of the parent window
        x11.XGetWMName(display, parentWindowRef.getValue(), parentname);
        
        dupla.setValue1(parentname.value);
        
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

}
