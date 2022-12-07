/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import io.reactivex.rxjava3.core.Maybe;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
@Slf4j
public class ExportedGroupServiceImpl implements ExportedGroupService {
    
    public static final DbCache<ExportedGroupId,ExportedGroup> dbCache = DbCacheFactory.getDbCache(ExportedGroupRepository.getInstance(),
            exportedGroup -> new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile()),
            ExportedGroupServiceImpl::elementsFromResultSet);

    @Override
    public Single<Long> add(ExportedGroup element) {
        if (element == null) {
            return Single.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            log.error("File path cannot be longer than 500 characters");
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(ExportedGroup element) {
        if (element == null) {
            return Single.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            log.error("File path cannot be longer than 500 characters");
            return Single.just(-1L);
        }
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<ExportedGroup> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<ExportedGroup> findById(ExportedGroupId id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Long> deleteById(ExportedGroupId id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }

    @Override
    public Observable<ExportedGroup> findByGroup(long id) {
        return dbCache.getAll().filter(eg -> id == eg.getGroup());
    }

    @Override
    public Observable<ExportedGroup> findByFileAndGroup(long groupId, String file) {
        return dbCache.getAll().filter(eg -> groupId == eg.getGroup()).filter(eg -> StringUtils.equals(file, eg.getFile()));
    }

    @Override
    public Single<Long> deleteByGroup(long id) {
        return dbCache.getAll()
                .filter(eg -> id == eg.getGroup())
                .flatMapSingle(eg -> dbCache.remove(new ExportedGroupId(eg.getGroup(), eg.getFile())))
                .filter(Boolean::booleanValue)
                .count();
    }

    private static Observable<ExportedGroup> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscribe -> {
            try {
                while (set.next()) {
                    subscribe.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscribe.onError(ex);
            }
            subscribe.onComplete();
        });
    }

    private static ExportedGroup elementFromResultSetEntry(ResultSet set) {
        ExportedGroup el = new ExportedGroup();
        try {
            el.setGroup(set.getLong(ExportedGroup.GROUP));
            el.setFile(set.getString(ExportedGroup.FILE));
            el.setDays(set.getLong(ExportedGroup.DAYS));
        } catch (SQLException ex) {
            log.error("Error extracting ExportedGroup from ResultSet", ex);
        }
        return el;
    }

}
