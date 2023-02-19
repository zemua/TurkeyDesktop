package devs.mrp.turkeydesktop.service.processchecker;

public class ProcessCheckerFactoryImpl implements ProcessCheckerFactory {
    
    private static ProcessCheckerFactoryImpl instance;
    
    private ProcessCheckerFactoryImpl() {}
    
    public static ProcessCheckerFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ProcessCheckerFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public ProcessChecker getNew() {
        return new ProcessCheckerImpl(this);
    }

    @Override
    public ProcessInfo getNewProcessInfo() {
        return ProcessInfoFactoryImpl.getInstance().getNew();
    }
    
}
