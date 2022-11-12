/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

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
public class ExportedGroupServiceImpl implements ExportedGroupService {

    private static final ExportedGroupDao repo = ExportedGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExportedGroupServiceImpl.class.getName());

    @Override
    public Single<Long> add(ExportedGroup element) {
        if (element == null) {
            return Single.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return repo.findById(element.getGroup()).flatMap(rs -> {
            try {
                if (rs.next()) {
                    ExportedGroup group = elementFromResultSetEntry(rs);
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
    public Single<Long> update(ExportedGroup element) {
        if (element == null) {
            return Single.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return Single.just(-1L);
        }
        return repo.update(element);
    }

    @Override
    public Observable<ExportedGroup> findAll() {
        return repo.findAll().flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Single<ExportedGroup> findById(long id) {
        return repo.findById(id).map(set -> {
            ExportedGroup element = null;
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
    public Observable<ExportedGroup> findByGroup(long id) {
        return repo.findByGroup(id).flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Observable<ExportedGroup> findByFileAndGroup(long groupId, String file) {
        return repo.findByGroupAndFile(groupId, file).flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Single<Long> deleteByGroup(long id) {
        return repo.deleteByGroup(id);
    }

    private Observable<ExportedGroup> elementsFromResultSet(ResultSet set) {
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

    private ExportedGroup elementFromResultSetEntry(ResultSet set) {
        ExportedGroup el = new ExportedGroup();
        try {
            el.setGroup(set.getLong(ExportedGroup.GROUP));
            el.setFile(set.getString(ExportedGroup.FILE));
            el.setDays(set.getLong(ExportedGroup.DAYS));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

}
