package devs.mrp.turkeydesktop.database.imports;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface ImportService {
    public Single<Long> add(String path);
    public Observable<String> findAll();
    public Single<Boolean> exists(String path);
    public Single<Long> deleteById(String path);
}
