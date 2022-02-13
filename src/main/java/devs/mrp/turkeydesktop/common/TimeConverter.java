/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import java.util.Formatter;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author miguel
 */
public class TimeConverter {

    public static String millisToHMS(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    
    public static String millisToHM(long millis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1));
    }

    public static long getHours(long millis) {
        return millis/(1000*60/60);
    }
    
    public static long hoursToMilis(long hours) {
        return hours*60*60*1000;
    }
    
    public static long minutesToMilis(long minutes) {
        return minutes*60*1000;
    }
    
    public static long getMinutes(long millis) {
        return (millis%(1000*60*60))/(1000*60);
    }
    
    public static long getSeconds(long millis) {
        return (millis%(1000*60))/(1000);
    }
    
    public static String getFormatedHMS(Long milis){
        Formatter formatter = new Formatter();
        if (milis < 0){
            formatter.format("[ - %02d:%02d:%02d ]", getHours(-milis), getMinutes(-milis), getSeconds(-milis));
        } else {
            formatter.format("%02d:%02d:%02d", getHours(milis), getMinutes(milis), getSeconds(milis));
        }
        return formatter.toString();
    }
    
    public static long daysFromMillis(long milliseconds) {
        return TimeUnit.MILLISECONDS.toDays(milliseconds);
    }
    
    public static long millisFromDays(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }
    
    public static long currentDay() {
        return daysFromMillis(System.currentTimeMillis());
    }
    
    public static long offsetDay(long nDays) {
        return currentDay()-nDays;
    }
    
    public static long offsetDayInMillis(long nDays) {
        return millisFromDays(offsetDay(nDays));
    }
    
    public static long millisToBeginningOfDay(long milliseconds) {
        var days = daysFromMillis(milliseconds);
        return millisFromDays(days);
    }
    
    public static long millisToEndOfDay(long milliseconds) {
        var days = daysFromMillis(milliseconds);
        return millisFromDays(days) + millisFromDays(1);
    }
    
}
