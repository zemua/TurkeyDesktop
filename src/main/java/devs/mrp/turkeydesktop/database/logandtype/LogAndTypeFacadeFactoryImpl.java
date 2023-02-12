package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import java.util.List;
import java.util.function.Consumer;

public class LogAndTypeFacadeFactoryImpl implements LogAndTypeFacadeFactory {
    
    private FactoryInitializer factory;
    
    public LogAndTypeFacadeFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public LogAndTypeFacadeService getService() {
        return new LogAndTypeFacadeServiceImpl(this);
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
        return LogAndTypeFacadeRepository.getInstance(this);
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
    
}
