package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionServiceImpl implements ConditionService {
    
    private final DbCache<Long,Condition> dbCache;
    
    public ConditionServiceImpl(ConditionFactory conditionFactory) {
        dbCache = conditionFactory.getDbCache();
    }
    
    @Override
    public Single<Long> add(Condition condition) {
        if (ConditionValidator.isInvalidCondition(condition)) {
            return Single.just(-1L);
        }
        return dbCache.save(condition).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(Condition element) {
        if (element == null) {
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<Condition> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<Condition> findById(Long id) {
        return dbCache.read(id);
    }
    
    @Override
    public Observable<Condition> findByGroupId(Long groupId) {
        return dbCache.getAll().filter(c -> groupId.equals(c.getGroupId()));
    }

    @Override
    public Single<Long> deleteById(Long id) {
        return dbCache.remove(id).map(b -> b ? 1L : 0L);
    }
    
    @Override
    public Single<Long> deleteByGroupId(long id) {
        return findByGroupId(id)
                .flatMapSingle(c -> dbCache.remove(c.getId()))
                .filter(Boolean::booleanValue)
                .count();
    }
    
    @Override
    public Single<Long> deleteByTargetId(long id) {
        return dbCache.getAll()
                .filter(c -> id == c.getTargetId())
                .flatMapSingle(c -> dbCache.remove(c.getId()))
                .filter(Boolean::booleanValue)
                .count();
    }
    
}
