/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.database.config.ConfigElementFactory;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class TimeConverter {
    
    private static IConfigElementService configService = ConfigElementFactory.getService();

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
        return millis/(1000*60*60);
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
        return LocalDate.now().toEpochDay();
    }
    
    public static long offsetDay(long nDays) {
        return LocalDate.now().minusDays(nDays).toEpochDay();
    }
    
    public static long offsetDayInMillis(long nDays) {
        //return millisFromDays(offsetDay(nDays));
        return beginningOfOffsetDays(nDays);
    }
    
    public static long millisToBeginningOfDay(long milliseconds) {
        LocalDateTime start = LocalDate.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()).atStartOfDay();
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public static long millisToEndOfDay(long milliseconds) {
        LocalDateTime start = LocalDate.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()).atStartOfDay().plusDays(1);
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public static long endOfToday() {
        return millisToEndOfDay(System.currentTimeMillis());
    }
    
    public static Single<Long> endOfTodayConsideringDayChange() {
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY)
                .map(changeOfDayResult -> {
                    Long changeOfDayMilis = hoursToMilis(Long.valueOf(changeOfDayResult.getValue()));
                    return endOfToday()+changeOfDayMilis;
                });
    }
    
    public static long beginningOfOffsetDays(long offsetDays) {
        LocalDateTime start = LocalDate.now().atStartOfDay().minusDays(offsetDays);
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public static Single<Long> beginningOfOffsetDaysConsideringDayChange(long offsetDays) {
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY)
                .map(changeOfDayResult -> {
                    Long changeOfDay = Long.valueOf(changeOfDayResult.getValue());
                    LocalDateTime start = LocalDateTime.now().minusHours(changeOfDay).toLocalDate().atStartOfDay().minusDays(offsetDays).plusHours(changeOfDay);
                    ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
                    return zdt.toInstant().toEpochMilli();
                });
    }
    
    public static long endOfOffsetDays(long offsetDays) {
        LocalDateTime start = LocalDate.now().atStartOfDay().plusHours(24).minusDays(offsetDays);
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public static Single<Long> endOfOffsetDaysConsideringDayChange(long offsetDays) {
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY)
                .map(changeOfDayResult -> {
                    Long changeOfDay = Long.valueOf(changeOfDayResult.getValue());
                    LocalDateTime end = LocalDateTime.now().minusHours(changeOfDay).toLocalDate().atStartOfDay().plusHours(24).minusDays(offsetDays).plusHours(changeOfDay);
                    ZonedDateTime zdt = end.atZone(ZoneId.systemDefault());
                    return zdt.toInstant().toEpochMilli();
                });
    }
    
    public static long epochToMilisOnGivenDay(long epoch) {
        LocalTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()).toLocalTime();
        return time.getLong(ChronoField.MILLI_OF_DAY);
    }
    
}
