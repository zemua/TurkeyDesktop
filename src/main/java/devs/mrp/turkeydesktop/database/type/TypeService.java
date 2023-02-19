package devs.mrp.turkeydesktop.database.type;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface TypeService {
    
    public Single<Long> add(Type element);
    public Single<Long> update(Type element);
    public Observable<Type> findAll();
    public Maybe<Type> findById(String id);
    public Single<Long> deleteById(String id);
    public Observable<Type> findByType(Type.Types type);
    
}
