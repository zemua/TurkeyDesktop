package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritter;
import devs.mrp.turkeydesktop.service.processkiller.KillerChainCommander;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainBaseHandler;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainFactory;
import java.awt.Image;

public class WatchDogFactoryImpl implements WatchDogFactory {
    
    private FactoryInitializer factory;
    
    public WatchDogFactoryImpl(FactoryInitializer initializer) {
        this.factory = initializer;
    }
    
    @Override
    public WatchDog getInstance() {
        return WatchDogImpl.getInstance(this);
    }

    @Override
    public ConditionChecker getConditionChecker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
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
        return TrayChainFactory.getChain();
    }

    @Override
    public ChainHandler<String> getKillerHandler() {
        return new KillerChainCommander().getHandlerChain();
    }

    @Override
    public ResourceHandler<Image, ImagesEnum> getImageHandler() {
        return ResourceHandlerFactory.getImagesHandler();
    }
    
}
