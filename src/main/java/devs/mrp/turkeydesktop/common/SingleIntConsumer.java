/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ncm55070
 */
public class SingleIntConsumer implements IntConsumer {
    
    private static final Logger LOGGER = Logger.getLogger(SingleIntConsumer.class.getSimpleName());
    private static final LocaleMessages locale = LocaleMessages.getInstance();
    
    private boolean consumed = false;
    private final IntConsumer mConsumer;
    
    public SingleIntConsumer(IntConsumer consumer) {
        mConsumer = consumer;
    }

    @Override
    public void accept(int t) {
        if (consumed == false) {
            consumed = true;
            mConsumer.accept(t);
        } else {
            LOGGER.log(Level.WARNING, "Tried to consume content more than one time, stack: {0}", Arrays.toString(Thread.currentThread().getStackTrace()));
            Toaster.sendToast(locale.getString("calledMoreThanOneTime"));
        }
    }
    
}
