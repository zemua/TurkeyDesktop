package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactoryImpl;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.GroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleFactoryImpl;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeFactoryImpl;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactoryImpl;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.function.Consumer;

public class LogAndTypeFacadeFactoryImpl implements LogAndTypeFacadeFactory {
    
    private static LogAndTypeFacadeFactoryImpl instance;
    private static LogAndTypeFacadeService logAndTypeFacadeService;
    private static LogAndTypeFacadeRepository logAndTypeFacadeRepository;
    
    private LogAndTypeFacadeFactoryImpl() {}
    
    public static LogAndTypeFacadeFactoryImpl getInstance() {
        if (instance == null) {
            instance = new LogAndTypeFacadeFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public LogAndTypeFacadeService getService() {
        if (logAndTypeFacadeService == null) {
            logAndTypeFacadeService = new LogAndTypeFacadeServiceImpl(this);
        }
        return logAndTypeFacadeService;
    }
    
    @Override
    public Consumer<List<Tripla<String, Long, Type.Types>>> getTriplaConsumer(Consumer<List<Tripla<String, Long, Type.Types>>> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }
    
    @Override
    public Consumer<TimeLog> getConsumer(Consumer<TimeLog> consumer) {
        return new SingleConsumer<>(consumer, CommonBeans.getToaster());
    }

    @Override
    public ConditionChecker conditionChecker() {
        return ConditionCheckerFactoryImpl.getInstance().getConditionChecker();
    }

    @Override
    public ConfigElementService configService() {
        return ConfigElementFactoryImpl.getInstance().getService();
    }
    
    @Override
    public Toaster getToaster() {
        return CommonBeans.getToaster();
    }

    @Override
    public Db getDb() {
        return DbFactoryImpl.getInstance().getDb();
    }

    @Override
    public LogAndTypeFacadeDao getRepo() {
        if (logAndTypeFacadeRepository == null) {
            logAndTypeFacadeRepository = new LogAndTypeFacadeRepository(this);
        }
        return logAndTypeFacadeRepository;
    }

    @Override
    public GroupAssignationService getGroupAssignationService() {
        return GroupAssignationFactoryImpl.getInstance().getService();
    }

    @Override
    public CloseableService getCloseableService() {
        return CloseableFactoryImpl.getInstance().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return TimeLogFactoryImpl.getInstance().getService();
    }

    @Override
    public TypeService getTypeService() {
        return TypeFactoryImpl.getInstance().getService();
    }

    @Override
    public TitleService getTitleService() {
        return TitleFactoryImpl.getInstance().getService();
    }

    @Override
    public Single<TimeLog> asBlockable(TimeLog timeLog) {
        return TimeLogFactoryImpl.getInstance().asBlockable(timeLog);
    }

    @Override
    public Single<TimeLog> asNotBlockable(TimeLog timeLog) {
        return TimeLogFactoryImpl.getInstance().asNotBlockable(timeLog);
    }

    @Override
    public GroupService getGroupService() {
        return GroupFactoryImpl.getInstance().getService();
    }
    
}
