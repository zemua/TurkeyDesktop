package devs.mrp.turkeydesktop.service.conditionchecker.imports;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;

public class ImportReaderFactoryImpl implements ImportReaderFactory {
    
    private static ImportReaderFactoryImpl instance;
    private static ImportReader importReader;
    
    private ImportReaderFactoryImpl() {}
    
    public static ImportReaderFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ImportReaderFactoryImpl();
        }
        return instance;
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
        return CommonBeans.getFileHandler();
    }
}
