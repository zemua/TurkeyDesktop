/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

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
public class ExternalGroupServiceImpl implements ExternalGroupService {
    
    public static final DbCache<Long,ExternalGroup> dbCache = DbCacheFactory.getDbCache(ExternalGroupRepository.getInstance(),
            ExternalGroup::getId,
            ExternalGroupServiceImpl::elementsFromResultSet);

    @Override
    public Single<Long> add(ExternalGroup element) {
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

    private static Observable<ExternalGroup> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                log.debug("error observing elementFromResultSet", ex);
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }

    private static ExternalGroup elementFromResultSetEntry(ResultSet set) {
        ExternalGroup el = new ExternalGroup();
        try {
            el.setId(set.getLong(ExternalGroup.ID));
            el.setGroup(set.getLong(ExternalGroup.GROUP));
            el.setFile(set.getString(ExternalGroup.FILE));
        } catch (SQLException ex) {
            log.error("Error mapping element from result set", ex);
        }
        return el;
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
