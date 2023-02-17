package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeServiceImpl implements TypeService {
    
    private final DbCache<String,Type> dbCache;
    
    public TypeServiceImpl(TypeFactory typeFactory) {
        this.dbCache = typeFactory.getDbCache();
    }

    @Override
    public Single<Long> add(Type type) {
        if (TypeValidator.isInvalid(type)) {
            return Single.just(-1L);
        }
        return dbCache.save(type).map(SaveAction::get);
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

}
