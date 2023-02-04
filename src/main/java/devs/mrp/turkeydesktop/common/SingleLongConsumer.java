package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import java.util.Arrays;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleLongConsumer implements LongConsumer {
    
    private static final Logger LOGGER = Logger.getLogger(SingleLongConsumer.class.getSimpleName());
    private static final LocaleMessages locale = LocaleMessages.getInstance();

    private boolean consumed = false;
    private final LongConsumer mConsumer;
    private final Toaster toaster;
    
    public SingleLongConsumer(LongConsumer consumer, Toaster toaster) {
        mConsumer = consumer;
        this.toaster = toaster;
    }

    @Override
    public void accept(long t) {
        if (consumed == false) {
            consumed = true;
            mConsumer.accept(t);
        } else {
            LOGGER.log(Level.WARNING, "Tried to consume content more than one time, stack: {0}", Arrays.toString(Thread.currentThread().getStackTrace()));
            toaster.sendToast(locale.getString("calledMoreThanOneTime"));
        }
    }
    
}
