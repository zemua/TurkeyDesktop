package devs.mrp.turkeydesktop.database.closeables;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface CloseableService {
    
    public Single<Long> add(String element);
    public Observable<Closeable> findAll();
    public Maybe<Closeable> findById(String id);
    public Single<Boolean> canBeClosed(String process);
    public Single<Long> deleteById(String id);
    
}
