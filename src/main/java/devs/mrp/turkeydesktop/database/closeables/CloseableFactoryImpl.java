package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseableFactoryImpl implements CloseableFactory {
    
    private final FactoryInitializer factory;
    private final DbCache<String, Closeable> dbCache;
    private final CloseableService closeableService;
    
    public CloseableFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
        this.dbCache = buildCache();
        this.closeableService = new CloseableServiceImpl(this);
    }
    
    private DbCache<String, Closeable> buildCache() {
        return DbCacheFactory.getDbCache(new CloseableRepository(this),
            Closeable::getProcess,
            key -> CloseableValidator.isValidKey(key),
            this::listFromResultSet,
            (process,key) -> process);
    }
    
    @Override
    public DbCache<String, Closeable> getDbCache() {
        return dbCache;
    }
    
    @Override
    public CloseableService getService() {
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
