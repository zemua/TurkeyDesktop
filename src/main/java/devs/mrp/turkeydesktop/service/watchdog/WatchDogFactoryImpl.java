package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritter;
import devs.mrp.turkeydesktop.service.processchecker.ProcessChecker;
import devs.mrp.turkeydesktop.service.processkiller.KillerChainCommander;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.watchdog.logger.DbLogger;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainBaseHandler;
import java.awt.Image;

public class WatchDogFactoryImpl implements WatchDogFactory {
    
    private WatchDog watchDog;
    private static WatchDogFactoryImpl instance;
    
    private WatchDogFactoryImpl() {
    }
    
    public static WatchDogFactoryImpl getInstance() {
        if (instance == null) {
            instance = new WatchDogFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public WatchDog getWatchDog() {
        if (watchDog == null) {
            watchDog = new WatchDogImpl(this);
        }
        return watchDog;
    }

    @Override
    public ConditionChecker getConditionChecker() {
        return ConditionCheckerFactoryImpl().getInstance().getConditionChecker();
    }

    @Override
    public Toaster getToaster() {
        return factory.getToaster();
    }

    @Override
    public FileHandler getFileHandler() {
        return factory.getFileHandler();
    }

    @Override
    public ExportWritter getExportWritter() {
        return factory.getExportWritterFactory().getWritter();
    }

    @Override
    public GroupService getGroupService() {
        return factory.getGroupFactory().getService();
    }

    @Override
    public TrayChainBaseHandler getTrayChainBaseHandler() {
        return factory.getTrayChainFactory().getChain();
    }

    @Override
    public ChainHandler<String> getKillerHandler() {
        return new KillerChainCommander().getHandlerChain();
    }

    @Override
    public ResourceHandler<Image, ImagesEnum> getImageHandler() {
        return ResourceHandlerFactory.getImagesHandler();
    }

    @Override
    public DbLogger getDbLogger() {
        return factory.getDbLoggerFactory().getNew();
    }

    @Override
    public ProcessChecker getProcessChecker() {
        return factory.getProcessCheckerFactory().getNew();
    }
    
}
