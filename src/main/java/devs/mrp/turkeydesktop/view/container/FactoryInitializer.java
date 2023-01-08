package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.closeables.Closeable;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableRepository;
import devs.mrp.turkeydesktop.database.closeables.CloseableValidator;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactory;
import devs.mrp.turkeydesktop.database.conditions.ConditionRepository;
import devs.mrp.turkeydesktop.database.conditions.ConditionValidator;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactory;
import devs.mrp.turkeydesktop.database.config.ConfigElementRepository;
import devs.mrp.turkeydesktop.database.config.ConfigElementValidator;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupFactory;
import devs.mrp.turkeydesktop.database.group.GroupRepository;
import devs.mrp.turkeydesktop.database.group.GroupValidator;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationRepository;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationServiceImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationValidator;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactory;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupId;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupRepository;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupValidator;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.imports.ImportValidator;
import devs.mrp.turkeydesktop.database.imports.ImportsRepository;
import devs.mrp.turkeydesktop.database.titles.*;
import java.util.function.Supplier;

class FactoryInitializer {
    
    private Supplier<Db> dbSupplier = () -> Db.getInstance();

    public void setDbSupplier(Supplier<Db> dbSupplier) {
        this.dbSupplier = dbSupplier;
    }
    
    private Supplier<Db> dbSupplier() {
        return dbSupplier;
    }
    
    public void initialize() {
        initGeneralDb();
        
        initTitleDbCache();
        initImportsDbCache();
        initConfigDbCache();
        initCloseableDbCache();
        initConditionDbCache();
        initGroupDbCache();
        initGroupAssignationDbCache();
        initExportedGroupDbCache();
        
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
    
    private void initConditionDbCache() {
        ConditionFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ConditionRepository.getInstance(),
            c -> c.getId(),
            key -> ConditionValidator.isValidKey(key),
            ConditionFactory::elementsFromResultSet));
    }
    
    private void initGroupDbCache() {
        GroupFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(GroupRepository.getInstance(),
            Group::getId,
            key -> GroupValidator.isValidKey(key),
            GroupFactory::elementsFromResultSet));
    }

    private void initGroupAssignationDbCache() {
        GroupAssignationFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(
            GroupAssignationRepository.getInstance(),
            element -> new GroupAssignationDao.ElementId(element.getType(), element.getElementId()),
            GroupAssignationValidator::isValidKey,
            GroupAssignationFactory::elementsFromResultSet));
    }
    
    private void initExportedGroupDbCache() {
        ExportedGroupFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ExportedGroupRepository.getInstance(),
            exportedGroup -> new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile()),
            ExportedGroupValidator::isValidKey,
            ExportedGroupFactory::elementsFromResultSet));
    }

    private void initGroupAssignationService() {
        GroupAssignationFactory.setGroupAssignationServiceSupplier(() -> new GroupAssignationServiceImpl());
    }
    
}
