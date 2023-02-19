package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.SingleConsumerFactory;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.DbFactory;
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
import devs.mrp.turkeydesktop.database.titles.*;
import devs.mrp.turkeydesktop.database.type.TypeFactory;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactoryImpl;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritterFactory;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReaderFactory;
import devs.mrp.turkeydesktop.service.processchecker.ProcessCheckerFactory;
import devs.mrp.turkeydesktop.service.processchecker.ProcessInfoFactory;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.toaster.voice.VoiceNotificator;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactory;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLoggerFactory;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactory;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesPanelFactory;
import devs.mrp.turkeydesktop.view.categorizetitles.element.conditions.TitleConditionsPanelFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactory;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainFactory;
import devs.mrp.turkeydesktop.view.groups.GroupsPanelFactory;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactory;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactoryImpl;
import devs.mrp.turkeydesktop.view.mainpanel.MainPanelFactory;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesPanelFactory;
import devs.mrp.turkeydesktop.view.times.TimesPanelFactory;
import lombok.Getter;

@Getter
public class FactoryInitializer {
    
    private DbFactory dbFactory;
    private WatchDogFactory watchDogFactory;
    private MainContainerFactory mainContainerFactory;
    
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
    private TitleFactory titleFactory;
    private TypeFactory typeFactory;
    
    private ExportWritterFactory exportWritterFactory;
    private ImportReaderFactory importReaderFactory;
    private DbLoggerFactory dbLoggerFactory;
    private ProcessCheckerFactory processCheckerFactory;
    private ProcessInfoFactory processInfoFactory;
    
    private MainPanelFactory mainPanelFactory;
    private GroupsPanelFactory groupsPanelFactory;
    private ConfigurationPanelFactory configurationPanelFactory;
    private CatProcessPanelFactory catProcessPanelFactory;
    private CategorizeTitlesPanelFactory categorizeTitlesPanelFactory;
    private TitleConditionsPanelFactory titleConditionsPanelFactory;
    private NotCloseablesPanelFactory notCloseablesPanelFactory;
    private TimesPanelFactory timesPanelFactory;
    
    private TrayChainFactory trayChainFactory;
    
    private VoiceNotificator voiceNotificator;
    private Toaster toaster;
    private SingleConsumerFactory singleConsumerFactory;
    private TimeConverter timeConverter;
    private FileHandler fileHandler;
    
    private FactoryInitializer() {
    }
    
    public static FactoryInitializer getNew() {
        FactoryInitializer initializer = new FactoryInitializer();
        initializer.initDbRequirements();
        return initializer.initialize();
    }
    
    public static FactoryInitializer getNew(DbFactory dbFactory) {
        FactoryInitializer initializer = new FactoryInitializer();
        initializer.initDbRequirements();
        initializer.dbFactory = dbFactory;
        return initializer.initialize();
    }
    
    private void initDbRequirements() {
        timeConverter = new TimeConverter(this);
        conditionCheckerFactory = new ConditionCheckerFactoryImpl(this);
    }
    
    private FactoryInitializer initialize() {
        configElementFactory = new ConfigElementFactoryImpl(this);
        fileHandler = new FileHandler(this);
        mainContainerFactory = new MainContainerFactoryImpl(this);
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
        titleFactory = new TitleFactoryImpl(this);
        typeFactory = new TypeFactoryImpl(this);
        exportWritterFactory = new ExportWritterFactoryImpl(this);
        importReaderFactory = new ImportReaderFactoryImpl(this);
        dbLoggerFactory = new DbLoggerFactoryImpl(this);
        processCheckerFactory = new ProcessCheckerFactoryImpl(this);
        processInfoFactory = new ProcessInfoFactoryImpl(this);
        mainPanelFactory = new MainPanelFactoryImpl(this);
        groupsPanelFactory = new GroupsPanelFactoryImpl(this);
        configurationPanelFactory = new ConfigurationPanelFactoryImpl(this);
        catProcessPanelFactory = new CatProcessPanelFactoryImpl(this);
        categorizeTitlesPanelFactory = new CategorizeTitlesPanelFactoryImpl(this);
        titleConditionsPanelFactory = new TitleConditionsPanelFactoryImpl(this);
        notCloseablesPanelFactory = new NotCloseablesPanelFactoryImpl(this);
        timesPanelFactory = new TimesPanelFactoryImpl(this);
        trayChainFactory = new TrayChainFactoryImpl(this);
        voiceNotificator = VoiceNotificator.getInstance(configElementFactory.getService());
        toaster = Toaster.getInstance(voiceNotificator);
        singleConsumerFactory = new SingleConsumerFactory(this);
        
        return this;
    }
    
}
