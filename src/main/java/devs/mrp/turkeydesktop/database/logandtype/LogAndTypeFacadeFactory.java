package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import java.util.List;
import java.util.function.Consumer;

public interface LogAndTypeFacadeFactory {
    
    ConditionChecker conditionChecker();
    ConfigElementService configService();
    LogAndTypeFacadeService getService();
    Consumer<List<Tripla<String, Long, Type.Types>>> getTriplaConsumer(Consumer<List<Tripla<String, Long, Type.Types>>> consumer);
    Consumer<TimeLog> getConsumer(Consumer<TimeLog> consumer);
    Db getDb();
    LogAndTypeFacadeDao getRepo();
    GroupAssignationService getGroupAssignationService();
    CloseableService getCloseableService();
    TimeConverter getTimeConverter();
    
}
