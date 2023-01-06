package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.closeables.Closeable;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableRepository;
import devs.mrp.turkeydesktop.database.closeables.CloseableValidator;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactory;
import devs.mrp.turkeydesktop.database.config.ConfigElementRepository;
import devs.mrp.turkeydesktop.database.config.ConfigElementValidator;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationServiceImpl;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.imports.ImportValidator;
import devs.mrp.turkeydesktop.database.imports.ImportsRepository;
import devs.mrp.turkeydesktop.database.titles.*;
import java.util.function.Supplier;

class FactoryInitializer {
    
    public void initialize() {
        initGeneralDb();
        
        initTitleDbCache();
        initImportsDbCache();
        initConfigDbCache();
        
        initGroupAssignationService();
    }
    
    private void initGeneralDb() {
        DbFactory.setDbSupplier(dbSupplier());
    }
    
    private void initTitleDbCache() {
        TitleFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(TitleRepository.getInstance(),
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            TitleFactory::elementsFromResultEntry));
    }
    
    private void initImportsDbCache() {
        ImportFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ImportsRepository.getInstance(),
            s -> s,
            key -> ImportValidator.isValidKey(key),
            ImportFactory::elementsFromSet));
    }
    
    private void initConfigDbCache() {
        ConfigElementFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ConfigElementRepository.getInstance(),
            c -> c.getKey().toString(),
            key -> ConfigElementValidator.isValidKey(key),
            ConfigElementFactory::elementsFromResultSet));
    }
    
    private void initCloseableDbCache() {
        CloseableFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(CloseableRepository.getInstance(),
            Closeable::getProcess,
            key -> CloseableValidator.isValidKey(key),
            CloseableFactory::listFromResultSet));
    }
    
    private void initGroupAssignationService() {
        GroupAssignationFactory.setGroupAssignationServiceSupplier(() -> new GroupAssignationServiceImpl());
    }
    
    private Supplier<Db> dbSupplier() {
        return () -> Db.getInstance();
    }
    
}
