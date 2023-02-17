package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.exporter.ExportWritter;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.container.traychain.TrayChainBaseHandler;
import java.awt.Image;

public interface WatchDogFactory {
    
    WatchDog getInstance();
    ConditionChecker getConditionChecker();
    Toaster getToaster();
    FileHandler getFileHandler();
    ExportWritter getExportWritter();
    GroupService getGroupService();
    TrayChainBaseHandler getTrayChainBaseHandler();
    ChainHandler<String> getKillerHandler();
    ResourceHandler<Image,ImagesEnum> getImageHandler();
    
}
