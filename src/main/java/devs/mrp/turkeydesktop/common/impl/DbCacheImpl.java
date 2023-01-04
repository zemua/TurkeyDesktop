package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.GeneralDao;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class DbCacheImpl<KEY, VALUE> implements DbCache<KEY, VALUE> {
    
    private Map<KEY, VALUE> cacheMap = Collections.synchronizedMap(new CacheMap<>(500));
    private GeneralDao<VALUE, KEY> repo;
    private Function<VALUE,KEY> keyExtractor;
    private Function<KEY,Boolean> isValidKey;
    private Function<ResultSet,Observable<VALUE>> listFromResultSet;
    
    public DbCacheImpl(GeneralDao<VALUE, KEY> repo,
            Function<VALUE,KEY> keyExtractor,
            Function<KEY,Boolean> isValidKey,
            Function<ResultSet,Observable<VALUE>> listFromResultSet) {
        this.repo = repo;
        this.keyExtractor = keyExtractor;
        this.isValidKey = isValidKey;
        this.listFromResultSet = listFromResultSet;
        loadDb();
    }

    @Override
    public Single<SaveAction> save(VALUE value) {
        KEY key = keyExtractor.apply(value);
        Maybe<KEY> result = addOrUpdate(key, value)
                .doOnSuccess(id -> cacheMap.put(id, value));
        return whichSaveAction(result);
    }
    
    private Single<SaveAction> whichSaveAction(Maybe<KEY> repoResult) {
        return repoResult.isEmpty().map(isEmpty -> isEmpty ? SaveAction.SAVED : SaveAction.EXISTING);
    }
    
    private Maybe<KEY> addOrUpdate(KEY key, VALUE value) {
        Maybe<KEY> result;
        if (canAddNew(key)) {
            result = Maybe.fromSingle(repo.add(value));
        } else if (canUpdate(key, value)) {
            result = Maybe.fromSingle(repo.update(value).map(qty -> key));
        } else {
            result = Maybe.empty();
        }
        return result;
    }
    
    private boolean canAddNew(KEY key) {
        return (isValidKey.apply(key) || Objects.isNull(cacheMap.get(key))) && !cacheMap.containsKey(key);
    }
    
    private boolean canUpdate(KEY key, VALUE value) {
        return isValidKey.apply(key) && cacheMap.containsKey(key) && !cacheMap.get(key).equals(value);
    }

    @Override
    public Maybe<VALUE> read(KEY key) {
        if (cacheMap.containsKey(key)) {
            return Maybe.just(cacheMap.get(key));
        }
        return Maybe.empty();
    }
    
    @Override
    public Single<Boolean> remove(KEY key) {
        if (cacheMap.containsKey(key)) {
            cacheMap.remove(key);
            return repo.deleteById(key).map(l -> l>0);
        }
        return Single.just(Boolean.FALSE);
    }

    @Override
    public Observable<VALUE> getAll() {
        return Observable.fromIterable(cacheMap.values());
    }

    @Override
    public boolean contains(KEY key) {
        return cacheMap.containsKey(key);
    }
    
    private void loadDb() {
        repo.findAll()
                .flatMapObservable(listFromResultSet::apply)
                .doOnNext(value -> cacheMap.put(keyExtractor.apply(value), value))
                .subscribe();
    }
    
}
