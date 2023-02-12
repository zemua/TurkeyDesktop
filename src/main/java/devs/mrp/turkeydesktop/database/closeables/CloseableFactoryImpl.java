package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseableFactoryImpl implements CloseableFactory {
    
    private FactoryInitializer factory;
    private static CloseableDao closeableRepository;
    private static CloseableService closeableService;
    
    public CloseableFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public DbCache<String, Closeable> getDbCache() {
        if (closeableRepository == null) {
            closeableRepository = new CloseableRepository(this);
        }
        return DbCacheFactory.getDbCache(closeableRepository,
            Closeable::getProcess,
            key -> CloseableValidator.isValidKey(key),
            this::listFromResultSet,
            (process,key) -> process);
    }
    
    @Override
    public CloseableService getService() {
        if (closeableService == null) {
            closeableService = new CloseableServiceImpl(this);
        }
        return closeableService;
    }
    
    @Override
    public Observable<Closeable> listFromResultSet(ResultSet set) {
        return Observable.create(suscriber -> {
            try {
                while(set.next()) {
                    suscriber.onNext(new Closeable(set.getString(Closeable.PROCESS_NAME)));
                }
            } catch (SQLException ex) {
                suscriber.onError(ex);
            }
            suscriber.onComplete();
        });
    }

    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }
    
}
