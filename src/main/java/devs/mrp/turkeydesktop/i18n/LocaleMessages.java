/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author miguel
 */
public class LocaleMessages {
    
    private static final String BUNDLE_NAME = "messages";
    
    private static LocaleMessages instance;
    
    Locale currentLocale;
    ResourceBundle messages;
    
    private LocaleMessages() {
        setBundle();
    }
    
    public static LocaleMessages getInstance() {
        if (instance == null) {
            instance = new LocaleMessages();
        }
        return instance;
    }
    
    private void setBundle() {
        setLocale();
        setMessages();
    }
    
    private void setLocale() {
        currentLocale = Locale.getDefault();
    }
    
    private void setMessages() {
        messages = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }
    
    public String getString(String s) {
        return messages.getString(s);
    }
    
    public String language() {
        if (currentLocale != null) {
            currentLocale.getLanguage();
        }
        return Locale.getDefault().getLanguage();
    }
}
