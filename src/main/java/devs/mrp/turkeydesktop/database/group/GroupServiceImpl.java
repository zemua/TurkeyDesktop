/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class GroupServiceImpl implements GroupService {
    
    private static final GroupDao repo = GroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupServiceImpl.class.getName());
    
    @Override
    public Single<Long> add(Group element) {
        if (element == null) {
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return repo.findById(element.getId()).flatMap(rs -> {
            try {
                if (rs.next()) {
                    Group group = elementFromResultSetEntry(rs);
                    // if the value stored differs from the one received
                    if (!group.equals(element)) {
                        return update(element);
                    }
                } else {
                    // else there is no element stored with this id
                    return repo.add(element);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return Single.just(0L);
        });
    }

    @Override
    public Single<Long> update(Group element) {
        if (element == null) {
            return Single.just(-1L);
        }
        return repo.update(element);
    }

    @Override
    public Observable<Group> findAll() {
        return repo.findAll().flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Single<Group> findById(long id) {
        return repo.findById(id).map(set -> {
            Group element = null;
            try {
                if (set.next()) {
                    element = elementFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return element;
        });
    }

    @Override
    public Single<Long> deleteById(long id) {
        return repo.deleteById(id);
    }

    @Override
    public Observable<Group> findAllPositive() {
        return repo.findAllOfType(Group.GroupType.POSITIVE).flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Observable<Group> findAllNegative() {
        return repo.findAllOfType(Group.GroupType.NEGATIVE).flatMapObservable(this::elementsFromResultSet);
    }
    
    @Override
    public Single<Integer> setPreventClose(long groupId, boolean preventClose) {
        return repo.setPreventClose(groupId, preventClose);
    }
    
    @Override
    public Single<Boolean> isPreventClose(long groupId) {
        if (groupId < 1) { // doesn't belong to a group
            return Single.just(false);
        }
        return findById(groupId).map(group -> group != null && group.isPreventClose());
    }
    
    private Observable<Group> elementsFromResultSet(ResultSet set) {
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
    
    private Group elementFromResultSetEntry(ResultSet set) {
        Group el = new Group();
        try {
            el.setId(set.getLong(Group.ID));
            el.setName(set.getString(Group.NAME));
            el.setType(Group.GroupType.valueOf(set.getString(Group.TYPE)));
            el.setPreventClose(set.getBoolean(Group.PREVENT_CLOSE));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }
    
}
