/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ncm55070
 */
public class SingleConsumer<T> implements Consumer<T> {
    
    private static final Logger LOGGER = Logger.getLogger(SingleConsumer.class.getSimpleName());
    private static final LocaleMessages locale = LocaleMessages.getInstance();
    
    private boolean consumed = false;
    private final Consumer<T> mConsumer;
    
    public SingleConsumer(Consumer<T> consumer) {
        mConsumer = consumer;
    }

    @Override
    public void accept(T t) {
        if (consumed == false) {
            consumed = true;
            mConsumer.accept(t);
        } else {
            LOGGER.log(Level.WARNING, "Tried to consume content more than one time, stack: {0}", Arrays.toString(Thread.currentThread().getStackTrace()));
            Toaster.sendToast(locale.getString("calledMoreThanOneTime"));
        }
    }
    
}
