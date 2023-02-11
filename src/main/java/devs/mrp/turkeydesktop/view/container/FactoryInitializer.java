package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.SingleConsumerFactory;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactoryImpl;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactory;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactory;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupFactory;
import devs.mrp.turkeydesktop.database.group.GroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationRepository;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationServiceImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationValidator;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupId;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupRepository;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupValidator;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupFactory;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupRepository;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupValidator;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeFactory;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.imports.ImportValidator;
import devs.mrp.turkeydesktop.database.imports.ImportsRepository;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeFactory;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeServiceFactoryImpl;
import devs.mrp.turkeydesktop.database.titles.*;
import devs.mrp.turkeydesktop.database.type.TypeFactory;
import devs.mrp.turkeydesktop.database.type.TypeRepository;
import devs.mrp.turkeydesktop.database.type.TypeValidator;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactoryImpl;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.toaster.voice.VoiceNotificator;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactory;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactoryImpl;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactory;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.groups.GroupsPanelFactory;
import devs.mrp.turkeydesktop.view.groups.GroupsPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactory;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactoryImpl;
import devs.mrp.turkeydesktop.view.mainpanel.MainPanelFactory;
import devs.mrp.turkeydesktop.view.mainpanel.MainPanelFactoryImpl;
import lombok.Getter;

@Getter
public class FactoryInitializer {
    
    private DbFactory dbFactory;
    private WatchDogFactory watchDogFactory;
    
    private ConfigElementFactory configElementFactory;
    private ConditionCheckerFactory conditionCheckerFactory;
    private GroupConditionFacadeFactory groupConditionFacadeFactory;
    private GroupReviewFactory groupReviewFactory;
    private LogAndTypeFacadeFactory logAndTypeFacadeFactory;
    private CloseableFactory closeableFactory;
    private ConditionFactory conditionFactory;
    private GroupFactory groupFactory;
    
    private MainPanelFactory mainPanelFactory;
    private GroupsPanelFactory groupsPanelFactory;
    private ConfigurationPanelFactory configurationPanelFactory;
    private CatProcessPanelFactory catProcessPanelFactory;
    
    private VoiceNotificator voiceNotificator;
    private Toaster toaster;
    private SingleConsumerFactory singleConsumerFactory;
    private TimeConverter timeConverter;
    private FileHandler fileHandler;
    
    private FactoryInitializer() {
    }
    
    public static FactoryInitializer getNew() {
        FactoryInitializer initializer = new FactoryInitializer();
        initializer.dbFactory = DbFactoryImpl.getNewFactory(initializer);
        initializer.initialize();
        return initializer;
    }
    
    public static FactoryInitializer getNew(DbFactory dbFactory) {
        FactoryInitializer initializer = new FactoryInitializer();
        initializer.dbFactory = dbFactory;
        initializer.initialize();
        return initializer;
    }
    
    private FactoryInitializer initialize() {
        watchDogFactory = new WatchDogFactoryImpl(this);
        configElementFactory = new ConfigElementFactoryImpl(this);
        conditionCheckerFactory = new ConditionCheckerFactoryImpl(this);
        groupConditionFacadeFactory = new GroupConditionFacadeFactoryImpl(this);
        groupReviewFactory = new GroupReviewFactoryImpl(this);
        logAndTypeFacadeFactory = new LogAndTypeServiceFactoryImpl(this);
        closeableFactory = new CloseableFactoryImpl(this);
        conditionFactory = new ConditionFactoryImpl(this);
        groupFactory = new GroupFactoryImpl(this);
        
        mainPanelFactory = new MainPanelFactoryImpl(this);
        groupsPanelFactory = new GroupsPanelFactoryImpl(this);
        configurationPanelFactory = new ConfigurationPanelFactoryImpl(this);
        catProcessPanelFactory = new CatProcessPanelFactoryImpl(this);
        
        voiceNotificator = VoiceNotificator.getInstance(configElementFactory.getService());
        toaster = Toaster.getInstance(voiceNotificator);
        singleConsumerFactory = new SingleConsumerFactory(this);
        timeConverter = new TimeConverter(this);
        fileHandler = new FileHandler(this);
        
        initTitleDbCache();
        initImportsDbCache();
        initGroupAssignationDbCache();
        initExportedGroupDbCache();
        initExternalGroupDbCache();
        
        initTypeDb();
        initTypeDbCache();
        initTypeRepo();
        
        initGroupAssignationService();
        
        return this;
    }
    
    private void initTitleDbCache() {
        TitleFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(TitleRepository.getInstance(),
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            TitleFactory::elementsFromResultEntry,
            (title, key) -> title));
    }
    
    private void initImportsDbCache() {
        ImportFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ImportsRepository.getInstance(),
            s -> s,
            key -> ImportValidator.isValidKey(key),
            ImportFactory::elementsFromSet,
            (path,key) -> path));
    }

    private void initGroupAssignationDbCache() {
        GroupAssignationFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(
            GroupAssignationRepository.getInstance(),
            element -> new GroupAssignationDao.ElementId(element.getType(), element.getElementId()),
            GroupAssignationValidator::isValidKey,
            GroupAssignationFactory::elementsFromResultSet,
            (assignation,id) -> assignation));
    }
    
    private void initExportedGroupDbCache() {
        ExportedGroupFactoryImpl.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ExportedGroupRepository.getInstance(),
            exportedGroup -> new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile()),
            ExportedGroupValidator::isValidKey,
            ExportedGroupFactoryImpl::elementsFromResultSet,
            (exported,id) -> exported));
    }
    
    private void initExternalGroupDbCache() {
        ExternalGroupFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ExternalGroupRepository.getInstance(),
            ExternalGroup::getId,
            ExternalGroupValidator::isValidKey,
            ExternalGroupFactory::elementsFromResultSet,
            (external,id) -> {
                external.setId(id);
                return external;
            }));
    }
    
    private void initTypeDb() {
        TypeFactory.setDbSupplier(() -> dbFactory.getDb());
    }
    
    private void initTypeDbCache() {
        TypeFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(TypeRepository.getInstance(),
            type -> type.getProcess(),
            TypeValidator::isValidKey,
            TypeFactory::listFromResultSet,
            (type,key) -> type));
    }
    
    private void initTypeRepo() {
        TypeFactory.setRepoSupplier(() -> TypeRepository.getInstance());
    }

    private void initGroupAssignationService() {
        GroupAssignationFactory.setGroupAssignationServiceSupplier(() -> new GroupAssignationServiceImpl());
    }
    
}
