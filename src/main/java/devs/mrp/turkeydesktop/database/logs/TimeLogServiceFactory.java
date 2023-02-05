package devs.mrp.turkeydesktop.database.logs;

public class TimeLogServiceFactory {
    
    public static TimeLogService getService() {
        return new TimeLogServiceImpl();
    }
    
}
