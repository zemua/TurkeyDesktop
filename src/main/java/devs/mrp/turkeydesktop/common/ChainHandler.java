/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

/**
 *
 * @author miguel
 */
public abstract class ChainHandler<T> {
    protected ChainHandler<T> mNextHandler;

    public void setNextHandler(ChainHandler handler) {
        mNextHandler = handler;
    }

    protected abstract boolean canHandle(String tipo);

    public void receiveRequest(String tipo, T data) {
        if (!canHandle(tipo)) {
            if (mNextHandler != null) {
                mNextHandler.receiveRequest(tipo, data);
            }
            return;
        }
        handle(data);
    }

    protected abstract void handle(T data);
}
