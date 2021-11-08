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
        // These elements need to be freed afterwards
        X11.Display display = x11.XOpenDisplay(null);
        X11.WindowByReference winRef = new X11.WindowByReference();
        IntByReference intByReference = new IntByReference();
        X11.XTextProperty name = new X11.XTextProperty();
        // ###########################################

        xlib.XGetInputFocus(display, winRef, intByReference);

        x11.XGetWMName(display, winRef.getValue(), name);

        /*System.out.println("complete structure: " + name);
        System.out.println("name: " + name.value);
        System.out.println("intByReference: " + intByReference);
        System.out.println("winRef: " + winRef);
        System.out.println("winRef Value: " + winRef.getValue());
        System.out.println("display: " + display);
        System.out.println("");
        System.out.println("");
        System.out.println("");*/
        dupla.setValue1(name.value);
        
        //X11.Window root = x11.XDefaultRootWindow(display);
        X11.WindowByReference windowRef = new X11.WindowByReference();
        X11.WindowByReference parentRef = new X11.WindowByReference();
        PointerByReference childrenRef = new PointerByReference();
        IntByReference childCountRef = new IntByReference();
        x11.XQueryTree(display, winRef.getValue(), windowRef, parentRef, childrenRef, childCountRef);
        
        /*System.out.println("root window: " + windowRef);
        System.out.println("parent ref: " + parentRef);
        System.out.println("children ref: " + childrenRef);
        System.out.println("child count ref: " + childCountRef);*/
        
        X11.XTextProperty parentname = new X11.XTextProperty();
        x11.XGetWMName(display, parentRef.getValue(), parentname);
        System.out.println("parent name: " + parentname.value);
        
        // Free Memory
        x11.XFree(name.getPointer());
        x11.XFree(intByReference.getPointer());
        x11.XFree(winRef.getPointer());
        x11.XFree(display.getPointer());
    }

}
