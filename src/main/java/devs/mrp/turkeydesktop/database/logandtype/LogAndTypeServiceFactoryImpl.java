package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.SingleConsumer;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import java.util.List;
import java.util.function.Consumer;

public class LogAndTypeServiceFactoryImpl implements LogAndTypeFacadeFactory {
    
    private FactoryInitializer factory;
    
    public LogAndTypeServiceFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public LogAndTypeFacadeService getService() {
        return new LogAndTypeFacadeServiceImpl(this);
    }
    
    @Override
    public Consumer<List<Tripla<String, Long, Type.Types>>> getTriplaConsumer(Consumer<List<Tripla<String, Long, Type.Types>>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    @Override
    public Consumer<TimeLog> getConsumer(Consumer<TimeLog> consumer) {
        return new SingleConsumer<>(consumer);
    }

    @Override
    public ConditionChecker conditionChecker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
    }

    @Override
    public ConfigElementService configService() {
        return factory.getConfigElementFactory().getService();
    }
    
}
