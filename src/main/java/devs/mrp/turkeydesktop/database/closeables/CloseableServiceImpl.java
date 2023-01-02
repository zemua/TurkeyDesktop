/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import io.reactivex.rxjava3.core.Maybe;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class CloseableServiceImpl implements CloseableService {

    public static final DbCache<String,Closeable> dbCache = DbCacheFactory.getDbCache(CloseableRepository.getInstance(),
            Closeable::getProcess,
            key -> isValidKey(key),
            CloseableServiceImpl::listFromResultSet);
    
    private static boolean isValidKey(String elementName) {
        return elementName != null && !elementName.isEmpty();
    }

    @Override
    public Single<Long> add(String element) {
        if (element == null) {
            return Single.just(-1L);
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

    private static Observable<Closeable> listFromResultSet(ResultSet set) {
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

}
