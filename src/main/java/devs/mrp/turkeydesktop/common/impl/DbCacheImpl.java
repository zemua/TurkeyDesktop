/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author ncm55070
 */
public class DbCacheImpl<KEY, VALUE> implements DbCache<KEY, VALUE> {
    
    private Map<KEY, VALUE> map = Collections.synchronizedMap(new CacheMap<>(500));
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
        if (canAddNew(key)) {
            return repo.add(value)
                    .doOnSuccess(id -> map.put(id, value))
                    .map(r -> SaveAction.SAVED);
        }
        if (canUpdate(key, value)) {
            return repo.update(value)
                    .doOnSuccess(qty -> map.put(key,value))
                    .map(r -> SaveAction.UPDATED);
        }
        return Single.just(SaveAction.EXISTING);
    }
    
    private boolean canAddNew(KEY key) {
        return (isValidKey.apply(key) || Objects.isNull(map.get(key))) && !map.containsKey(key);
    }
    
    private boolean canUpdate(KEY key, VALUE value) {
        return isValidKey.apply(key) && map.containsKey(key) && !map.get(key).equals(value);
    }

    @Override
    public Maybe<VALUE> read(KEY key) {
        if (map.containsKey(key)) {
            return Maybe.just(map.get(key));
        }
        return Maybe.empty();
    }
    
    @Override
    public Single<Boolean> remove(KEY key) {
        if (map.containsKey(key)) {
            map.remove(key);
            return repo.deleteById(key).map(l -> l>0);
        }
        return Single.just(Boolean.FALSE);
    }

    @Override
    public Observable<VALUE> getAll() {
        return Observable.fromIterable(map.values());
    }

    @Override
    public boolean contains(KEY key) {
        return map.containsKey(key);
    }
    
    private void loadDb() {
        repo.findAll()
                .flatMapObservable(listFromResultSet::apply)
                .doOnNext(value -> map.put(keyExtractor.apply(value), value))
                .subscribe();
    }
    
}
