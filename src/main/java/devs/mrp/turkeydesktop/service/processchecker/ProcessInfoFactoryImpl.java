package devs.mrp.turkeydesktop.service.processchecker;

public class ProcessInfoFactoryImpl implements ProcessInfoFactory {
    
    private static ProcessInfoFactoryImpl instance;
    
    private ProcessInfoFactoryImpl() {}
    
    public static ProcessInfoFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ProcessInfoFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public ProcessInfo getNew() {
        return new ProcessInfoImpl();
    }
}
