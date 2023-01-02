/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

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
public class GroupServiceImpl implements GroupService {
    
    public static final DbCache<Long,Group> dbCache = DbCacheFactory.getDbCache(GroupRepository.getInstance(),
            Group::getId,
            key -> isValidKey(key),
            GroupServiceImpl::elementsFromResultSet);
    
    private static boolean isValidKey(Long rowId) {
        return rowId != null && rowId > 0;
    }
    
    @Override
    public Single<Long> add(Group element) {
        if (element == null) {
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(Group element) {
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<Group> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<Group> findById(long id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Long> deleteById(long id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }

    @Override
    public Observable<Group> findAllPositive() {
        return dbCache.getAll().filter(g -> Group.GroupType.POSITIVE.equals(g.getType()));
    }

    @Override
    public Observable<Group> findAllNegative() {
        return dbCache.getAll().filter(g -> Group.GroupType.NEGATIVE.equals(g.getType()));
    }
    
    @Override
    public Single<Integer> setPreventClose(long groupId, boolean preventClose) {
        return dbCache.read(groupId)
                .map(g -> {
                    g.setPreventClose(preventClose);
                    return g;
                })
                .flatMapSingle(g -> dbCache.save(g))
                .map(SaveAction::get)
                .defaultIfEmpty(0L)
                .map(Long::intValue);
    }
    
    @Override
    public Single<Boolean> isPreventClose(long groupId) {
        if (groupId < 1) { // doesn't belong to a group
            return Single.just(false);
        }
        return findById(groupId).map(group -> group != null && group.isPreventClose()).defaultIfEmpty(false);
    }
    
    private static Observable<Group> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscriber.onError(ex);
            }
            subscriber.onComplete();
        });
    }
    
    private static Group elementFromResultSetEntry(ResultSet set) {
        Group el = new Group();
        try {
            el.setId(set.getLong(Group.ID));
            el.setName(set.getString(Group.NAME));
            el.setType(Group.GroupType.valueOf(set.getString(Group.TYPE)));
            el.setPreventClose(set.getBoolean(Group.PREVENT_CLOSE));
        } catch (SQLException ex) {
            log.error("Error extracting Group from ResultSet", ex);
        }
        return el;
    }
    
}
