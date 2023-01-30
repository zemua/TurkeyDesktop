package devs.mrp.turkeydesktop.database.group.external;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface ExternalGroupService {
    public Single<Long> add(ExternalGroup element);
    public Single<Long> update(ExternalGroup element);
    public Observable<ExternalGroup> findAll();
    public Maybe<ExternalGroup> findById(long id);
    public Single<Long> deleteById(long id);
    public Observable<ExternalGroup> findByGroup(Long id);
    public Observable<ExternalGroup> findByFile(String file);
    public Single<Long> deleteByGroup(Long id);
}
