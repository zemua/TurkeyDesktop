/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import io.reactivex.rxjava3.core.Maybe;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class ConditionService implements IConditionService {
    
    public static final DbCache<Long,Condition> dbCache = DbCacheFactory.getDbCache(ConditionRepository.getInstance(),
            c -> c.getId(),
            ConditionService::elementsFromResultSet);
    
    @Override
    public Single<Long> add(Condition element) {
        if (element == null) {
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
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
    
    private static Observable<Condition> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while(set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }
    
    private static Condition elementFromResultSetEntry(ResultSet set) {
        Condition el = new Condition();
        try {
            el.setId(set.getLong(Condition.ID));
            el.setGroupId(set.getLong(Condition.GROUP_ID));
            el.setTargetId(set.getLong(Condition.TARGET_ID));
            el.setUsageTimeCondition(set.getLong(Condition.USAGE_TIME_CONDITION));
            el.setLastDaysCondition(set.getLong(Condition.LAST_DAYS_CONDITION));
        } catch (SQLException ex) {
            log.error("Error creating Condition from ResultSet", ex);
        }
        return el;
    }
    
}
