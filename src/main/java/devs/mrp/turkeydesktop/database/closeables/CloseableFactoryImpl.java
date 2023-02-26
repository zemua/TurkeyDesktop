package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CloseableFactoryImpl implements CloseableFactory {
    
    private static CloseableFactoryImpl instance;
    private static DbCache<String, Closeable> dbCache;
    private static CloseableService closeableService;
    
    protected CloseableFactoryImpl(){}
    
    public static CloseableFactoryImpl getInstance() {
        if (instance == null) {
            instance = new CloseableFactoryImpl();
        }
        return instance;
    }
    
    protected DbCache<String, Closeable> buildCache(CloseableDao repo) {
        return DbCacheFactory.getDbCache(repo,
            Closeable::getProcess,
            key -> CloseableValidator.isValidKey(key),
            this::listFromResultSet,
            (process,key) -> process);
    }
    
    @Override
    public DbCache<String, Closeable> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new CloseableRepository(this));
        }
        return dbCache;
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
        return DbFactoryImpl.getInstance().getDb();
    }
    
}
