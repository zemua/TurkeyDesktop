package devs.mrp.turkeydesktop.service.processchecker;

import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class ProcessInfoFactoryImpl implements ProcessInfoFactory {
    
    private FactoryInitializer factory;
    
    public ProcessInfoFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public ProcessInfo getNew() {
        return new ProcessInfoImpl();
    }
}
