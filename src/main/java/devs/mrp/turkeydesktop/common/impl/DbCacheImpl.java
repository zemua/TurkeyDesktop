/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.impl;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.GeneralDao;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 *
 * @author ncm55070
 */
public class DbCacheImpl<KEY, VALUE> implements DbCache<KEY, VALUE> {
    
    private Map<KEY, VALUE> map = new ConcurrentHashMap<>();
    private GeneralDao<VALUE, KEY> repo;
    private Function<VALUE,KEY> keyExtractor;
    private Function<ResultSet,Observable<VALUE>> listFromResultSet;
    
    public DbCacheImpl(GeneralDao<VALUE, KEY> repo,
            Function<VALUE,KEY> keyExtractor,
            Function<ResultSet,Observable<VALUE>> listFromResultSet) {
        this.repo = repo;
        this.keyExtractor = keyExtractor;
        this.listFromResultSet = listFromResultSet;
        loadDb();
    }

    @Override
    public Single<SaveAction> save(VALUE value) {
        KEY key = keyExtractor.apply(value);
        if (Objects.isNull(map.get(key))) {
            map.put(key, value);
            return repo.add(value).map(r -> SaveAction.SAVED);
        }
        if (!map.get(key).equals(value)) {
            map.put(key, value);
            return repo.update(value).map(r -> SaveAction.UPDATED);
        }
        return Single.just(SaveAction.EXISTING);
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
