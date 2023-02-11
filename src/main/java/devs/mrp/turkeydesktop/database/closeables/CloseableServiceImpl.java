package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloseableServiceImpl implements CloseableService {

    public static DbCache<String,Closeable> dbCache;
    
    public CloseableServiceImpl(CloseableFactory closeableFactory) {
        dbCache = closeableFactory.getDbCache();
    }

    @Override
    public Single<Long> add(String element) {
        if (CloseableValidator.isInvalidKey(element)) {
            return Single.just(SaveAction.ERROR.get());
        }
        return dbCache.save(new Closeable(element)).map(SaveAction::get);
    }

    @Override
    public Observable<Closeable> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<Closeable> findById(String id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Boolean> canBeClosed(String process) {
        return dbCache.read(process).isEmpty();
    }

    @Override
    public Single<Long> deleteById(String id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }

}
