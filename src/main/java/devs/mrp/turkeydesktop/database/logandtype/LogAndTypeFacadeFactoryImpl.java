package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeFactory;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.function.Consumer;

public class LogAndTypeFacadeFactoryImpl implements LogAndTypeFacadeFactory {
    
    private FactoryInitializer factory;
    private static LogAndTypeFacadeService logAndTypeFacadeService;
    private static LogAndTypeFacadeRepository logAndTypeFacadeRepository;
    
    public LogAndTypeFacadeFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
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
        return new SingleConsumer<>(consumer, factory.getToaster());
    }
    
    @Override
    public Consumer<TimeLog> getConsumer(Consumer<TimeLog> consumer) {
        return new SingleConsumer<>(consumer, factory.getToaster());
    }

    @Override
    public ConditionChecker conditionChecker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
    }

    @Override
    public ConfigElementService configService() {
        return factory.getConfigElementFactory().getService();
    }

    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
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
        return factory.getGroupAssignationFactory().getService();
    }

    @Override
    public CloseableService getCloseableService() {
        return factory.getCloseableFactory().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return factory.getTimeLogServiceFactory().getService();
    }

    @Override
    public TypeService getTypeService() {
        return TypeFactory.getService();
    }

    @Override
    public TitleService getTitleService() {
        return factory.getTitleFactory().getService();
    }

    @Override
    public Single<TimeLog> asBlockable(TimeLog timeLog) {
        return factory.getTimeLogServiceFactory().asBlockable(timeLog);
    }

    @Override
    public Single<TimeLog> asNotBlockable(TimeLog timeLog) {
        return factory.getTimeLogServiceFactory().asNotBlockable(timeLog);
    }
    
}
