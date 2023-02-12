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
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactoryImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactory;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupFactory;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementFactory;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementFactoryImpl;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeFactory;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.imports.ImportFactoryImpl;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeFactory;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogFacadeFactory;
import devs.mrp.turkeydesktop.database.titledlog.TitledLogFacadeFactoryImpl;
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
    private GroupAssignationFactory groupAssignationFactory;
    private ExportedGroupFactory exportedGroupFactory;
    private ExternalGroupFactory externalGroupFactory;
    private AssignableElementFactory assignableElementFactory;
    private ImportFactory importFactory;
    private TimeLogFactory timeLogServiceFactory;
    private TitledLogFacadeFactory titledLogFacadeFactory;
    
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
        logAndTypeFacadeFactory = new LogAndTypeFacadeFactoryImpl(this);
        closeableFactory = new CloseableFactoryImpl(this);
        conditionFactory = new ConditionFactoryImpl(this);
        groupFactory = new GroupFactoryImpl(this);
        groupAssignationFactory = new GroupAssignationFactoryImpl(this) {};
        exportedGroupFactory = new ExportedGroupFactoryImpl(this);
        externalGroupFactory = new ExternalGroupFactoryImpl(this);
        assignableElementFactory = new AssignableElementFactoryImpl(this);
        importFactory = new ImportFactoryImpl(this);
        timeLogServiceFactory = new TimeLogFactoryImpl(this);
        titledLogFacadeFactory = new TitledLogFacadeFactoryImpl(this);
        
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
        
        initTypeDb();
        initTypeDbCache();
        initTypeRepo();
        
        return this;
    }
    
    private void initTitleDbCache() {
        TitleFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(TitleRepository.getInstance(),
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            TitleFactory::elementsFromResultEntry,
            (title, key) -> title));
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
    
}
