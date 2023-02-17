package devs.mrp.turkeydesktop.service.conditionchecker.imports;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class ImportReaderFactoryImpl implements ImportReaderFactory {
    
    private final FactoryInitializer factory;
    private static ImportReader importReader;
    
    public ImportReaderFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public ImportReader getReader() {
        if (importReader == null) {
            importReader = new ImportReaderImpl(this);
        }
        return importReader;
    }

    @Override
    public FileHandler getFileHandler() {
        return factory.getFileHandler();
    }
}
