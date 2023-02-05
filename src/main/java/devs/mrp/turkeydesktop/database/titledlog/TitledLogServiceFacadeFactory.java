package devs.mrp.turkeydesktop.database.titledlog;

public class TitledLogServiceFacadeFactory {
    
    public static TitledLogServiceFacade getService() {
        return new TitledLogServiceFacadeImpl();
    }
    
}
