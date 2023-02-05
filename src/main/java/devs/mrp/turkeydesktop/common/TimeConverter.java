package devs.mrp.turkeydesktop.common;

import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

public class TimeConverter {
    
    private final FactoryInitializer factory;
    
    public TimeConverter(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }

    public String millisToHMS(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    
    public String millisToHM(long millis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1));
    }

    public long getHours(long millis) {
        return millis/(1000*60*60);
    }
    
    public long hoursToMilis(long hours) {
        return hours*60*60*1000;
    }
    
    public long minutesToMilis(long minutes) {
        return minutes*60*1000;
    }
    
    public long getMinutes(long millis) {
        return (millis%(1000*60*60))/(1000*60);
    }
    
    public long getSeconds(long millis) {
        return (millis%(1000*60))/(1000);
    }
    
    public String getFormatedHMS(Long milis){
        Formatter formatter = new Formatter();
        if (milis < 0){
            formatter.format("[ - %02d:%02d:%02d ]", getHours(-milis), getMinutes(-milis), getSeconds(-milis));
        } else {
            formatter.format("%02d:%02d:%02d", getHours(milis), getMinutes(milis), getSeconds(milis));
        }
        return formatter.toString();
    }
    
    public long daysFromMillis(long milliseconds) {
        return TimeUnit.MILLISECONDS.toDays(milliseconds);
    }
    
    public long millisFromDays(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }
    
    public long currentDay() {
        return LocalDate.now().toEpochDay();
    }
    
    public long offsetDay(long nDays) {
        return LocalDate.now().minusDays(nDays).toEpochDay();
    }
    
    public long offsetDayInMillis(long nDays) {
        //return millisFromDays(offsetDay(nDays));
        return beginningOfOffsetDays(nDays);
    }
    
    public long millisToBeginningOfDay(long milliseconds) {
        LocalDateTime start = LocalDate.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()).atStartOfDay();
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public long millisToEndOfDay(long milliseconds) {
        LocalDateTime start = LocalDate.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault()).atStartOfDay().plusDays(1);
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public long endOfToday() {
        return millisToEndOfDay(System.currentTimeMillis());
    }
    
    public Single<Long> endOfTodayConsideringDayChange() {
        ConfigElementService configService = factory.getConfigElementFactory().getService();
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY)
                .map(changeOfDayResult -> {
                    Long changeOfDayMilis = hoursToMilis(Long.valueOf(changeOfDayResult.getValue()));
                    return endOfToday()+changeOfDayMilis;
                });
    }
    
    public long beginningOfOffsetDays(long offsetDays) {
        LocalDateTime start = LocalDate.now().atStartOfDay().minusDays(offsetDays);
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public Single<Long> beginningOfOffsetDaysConsideringDayChange(long offsetDays) {
        ConfigElementService configService = factory.getConfigElementFactory().getService();
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY)
                .map(changeOfDayResult -> {
                    Long changeOfDay = Long.valueOf(changeOfDayResult.getValue());
                    LocalDateTime start = LocalDateTime.now().minusHours(changeOfDay).toLocalDate().atStartOfDay().minusDays(offsetDays).plusHours(changeOfDay);
                    ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
                    return zdt.toInstant().toEpochMilli();
                });
    }
    
    public long endOfOffsetDays(long offsetDays) {
        LocalDateTime start = LocalDate.now().atStartOfDay().plusHours(24).minusDays(offsetDays);
        ZonedDateTime zdt = start.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
    
    public Single<Long> endOfOffsetDaysConsideringDayChange(long offsetDays) {
        ConfigElementService configService = factory.getConfigElementFactory().getService();
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY)
                .map(changeOfDayResult -> {
                    Long changeOfDay = Long.valueOf(changeOfDayResult.getValue());
                    LocalDateTime end = LocalDateTime.now().minusHours(changeOfDay).toLocalDate().atStartOfDay().plusHours(24).minusDays(offsetDays).plusHours(changeOfDay);
                    ZonedDateTime zdt = end.atZone(ZoneId.systemDefault());
                    return zdt.toInstant().toEpochMilli();
                });
    }
    
    public long epochToMilisOnGivenDay(long epoch) {
        LocalTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()).toLocalTime();
        return time.getLong(ChronoField.MILLI_OF_DAY);
    }
    
}
