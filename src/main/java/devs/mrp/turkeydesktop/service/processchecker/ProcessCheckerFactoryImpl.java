package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class ProcessCheckerFactoryImpl implements ProcessCheckerFactory {
    
    private FactoryInitializer factory;
    
    public ProcessCheckerFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public ProcessChecker getNew() {
        return new ProcessCheckerImpl(this);
    }

    @Override
    public ProcessInfo getNewProcessInfo() {
        return factory.getProcessInfoFactory().getNew();
    }
    
}
