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

import devs.mrp.turkeydesktop.common.ChainHandler;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author miguel
 */
public class CheckerChainHandlerLinux extends ChainHandler<Object> {

    public interface XLib extends StdCallLibrary {
        XLib INSTANCE = (XLib) Native.loadLibrary("XLib", XLib.class);
        int XGetInputFocus(X11.Display display, X11.Window focus_return, Pointer revert_to_return);
    }

    @Override
    protected boolean canHandle(String tipo) {
        return Platform.isLinux();
    }

    @Override
    protected void handle(Object data) {
        final X11 x11 = X11.INSTANCE;
        final XLib xlib= XLib.INSTANCE;
        X11.Display display = x11.XOpenDisplay(null);
        X11.Window window=new X11.Window();
        xlib.XGetInputFocus(display, window,Pointer.NULL);
        X11.XTextProperty name=new X11.XTextProperty();
        x11.XGetWMName(display, window, name);
        System.out.println(name.toString());
    }

}
