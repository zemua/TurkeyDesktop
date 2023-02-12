package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalGroupServiceImpl implements ExternalGroupService {
    
    private final DbCache<Long,ExternalGroup> dbCache;
    
    public ExternalGroupServiceImpl(ExternalGroupFactory externalGroupFactory) {
        this.dbCache = externalGroupFactory.getDbCache();
    }

    @Override
    public Single<Long> add(ExternalGroup externalGroup) {
        if (ExternalGroupValidator.isInvalid(externalGroup)) {
            log.error("Error adding invalid ExternalGroup: " + externalGroup);
            return Single.just(-1L);
        }
        return dbCache.save(externalGroup).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(ExternalGroup element) {
        if (element == null) {
            return Single.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            log.warn("File path cannot be longer than 500 characters");
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<ExternalGroup> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<ExternalGroup> findById(long id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Long> deleteById(long id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }

    @Override
    public Observable<ExternalGroup> findByGroup(Long id) {
        return dbCache.getAll().filter(eg -> id.equals(eg.getGroup()));
    }

    @Override
    public Observable<ExternalGroup> findByFile(String file) {
        return dbCache.getAll().filter(eg -> file.equals(eg.getFile()));
    }

    @Override
    public Single<Long> deleteByGroup(Long id) {
        return dbCache.getAll()
                .filter(eg -> id.equals(eg.getGroup()))
                .flatMapSingle(eg -> dbCache.remove(eg.getId()))
                .filter(Boolean::booleanValue).count();
    }

}
