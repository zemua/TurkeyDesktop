package devs.mrp.turkeydesktop.common;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface DbCache<KEY, VALUE> {
    public Single<SaveAction> save(VALUE value);
    public Maybe<VALUE> read(KEY key);
    public Single<Boolean> remove(KEY key);
    public Observable<VALUE> getAll();
    public boolean contains(KEY key);
}
