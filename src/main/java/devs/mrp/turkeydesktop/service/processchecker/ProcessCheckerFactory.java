package devs.mrp.turkeydesktop.service.processchecker;

public interface ProcessCheckerFactory {
    
    ProcessChecker getNew();
    ProcessInfo getNewProcessInfo();
    
}
