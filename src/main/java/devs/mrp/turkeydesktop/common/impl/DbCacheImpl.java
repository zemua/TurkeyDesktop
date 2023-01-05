package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.database.TurkeyDbException;
import devs.mrp.turkeydesktop.database.GeneralDao;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbCacheImpl<KEY, VALUE> implements DbCache<KEY, VALUE> {
    
    private Map<KEY, VALUE> cacheMap = Collections.synchronizedMap(new CacheMap<>(500));
    private GeneralDao<VALUE, KEY> repo;
    private Function<VALUE,KEY> keyExtractor;
    private Function<KEY,Boolean> isValidKey;
    private Function<ResultSet,Observable<VALUE>> streamFromResultSet;
    
    public DbCacheImpl(GeneralDao<VALUE, KEY> repo,
            Function<VALUE,KEY> keyExtractor,
            Function<KEY,Boolean> isValidKey,
            Function<ResultSet,Observable<VALUE>> streamFromResultSet) {
        this.repo = repo;
        this.keyExtractor = keyExtractor;
        this.isValidKey = isValidKey;
        this.streamFromResultSet = streamFromResultSet;
        loadDb();
    }

    @Override
    public Single<SaveAction> save(VALUE value) {
        KEY key = keyExtractor.apply(value);
        Single<KeyAndAction> updateOutput = addOrUpdate(key, value)
            .doOnSuccess(keyAndAction -> cacheMap.put(keyAndAction.key, value));
        return updateOutput.map(output -> output.action);
    }
    
    private Single<KeyAndAction> addOrUpdate(KEY key, VALUE value) throws TurkeyDbException {
        Single<KeyAndAction> result;
        if (canAddNew(key)) {
            result = repo.add(value).map(id -> new KeyAndAction(id, SaveAction.SAVED));
        } else if (canUpdate(key, value)) {
            result = repo.update(value).map(qty -> new KeyAndAction(key, SaveAction.UPDATED));
        } else if (existing(key, value)) {
            result = Single.just(new KeyAndAction(key, SaveAction.EXISTING));
        } else {
            log.error("Neither canAddNew, canUpdate, nor existing");
            throw new TurkeyDbException();
        }
        return result;
    }
    
    private class KeyAndAction {
        KEY key;
        SaveAction action;
        KeyAndAction(KEY k, SaveAction a) {
            key = k;
            action = a;
        }
    }
    
    private boolean canAddNew(KEY key) {
        return (isValidKey.apply(key) || Objects.isNull(cacheMap.get(key))) && !cacheMap.containsKey(key);
    }
    
    private boolean canUpdate(KEY key, VALUE value) {
        return isValidKey.apply(key) && cacheMap.containsKey(key) && !cacheMap.get(key).equals(value);
    }
    
    private boolean existing(KEY key, VALUE value) {
        return isValidKey.apply(key) && cacheMap.containsKey(key) && cacheMap.get(key).equals(value);
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
                .flatMapObservable(streamFromResultSet::apply)
                .doOnNext(value -> cacheMap.put(keyExtractor.apply(value), value))
                .subscribe();
    }
    
}
