package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportServiceImpl implements ImportService {
    
    public static final DbCache<String,String> dbCache = ImportFactory.getDbCache();

    @Override
    public Single<Long> add(String path) {
        Single<Long> result;
        if (ImportValidator.isInvalid(path)) {
            result = Single.just(-1L);
        } else {
            result = dbCache.save(path).map(SaveAction::get);
        }
        return result;
    }

    @Override
    public Observable<String> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Single<Boolean> exists(String path) {
        if (path == null || "".equals(path) || path.length() > 500) {
            return Single.just(false);
        }
        return dbCache.read(path).isEmpty().map(b -> !b);
    }

    @Override
    public Single<Long> deleteById(String path) {
        if (path == null) {
            return Single.just(-1L);
        }
        return dbCache.remove(path).map(b -> b?1L:0L);
    }

}
