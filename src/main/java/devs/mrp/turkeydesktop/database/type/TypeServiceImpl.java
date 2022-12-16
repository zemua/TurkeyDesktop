/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

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
public class TypeServiceImpl implements TypeService {
    
    TypeDao repo = TypeRepository.getInstance();
    
    private static final DbCache<String,Type> dbCache = DbCacheFactory.getDbCache(
            TypeRepository.getInstance(),
            (Type element) -> element.getProcess(),
            (ResultSet set) -> listFromResultSet(set));

    @Override
    public Single<Long> add(Type element) {
        if (element == null) {
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(Type element) {
        if (element == null || element.getProcess() == null) {
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<Type> findAll() {
        return dbCache.getAll();
    }
    
    @Override
    public Observable<Type> findByType(Type.Types type) {
        return dbCache.getAll().filter(t -> type.equals(t.getType()));
    }

    @Override
    public Maybe<Type> findById(String id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Long> deleteById(String id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }
    
    private static Observable<Type> listFromResultSet(ResultSet set) {
        return Observable.create(submitter -> {
            try {
                while (set.next()) {
                    Type type = new Type();
                    type.setProcess(set.getString(Type.PROCESS_NAME));
                    type.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
                    submitter.onNext(type);
                }
            } catch (SQLException ex) {
                log.error("error creating observable from resultSet", ex);
                submitter.onError(ex);
            }
            submitter.onComplete();
        });
    }
    
}
