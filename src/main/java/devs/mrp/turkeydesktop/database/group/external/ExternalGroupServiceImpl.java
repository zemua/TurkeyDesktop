/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public class ExternalGroupServiceImpl implements ExternalGroupService {

    private static final ExternalGroupDao repo = ExternalGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExternalGroupServiceImpl.class.getName());

    @Override
    public Single<Long> add(ExternalGroup element) {
        if (element == null) {
            return Single.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return Single.just(-1L);
        }
        // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
        return repo.findById(element.getId()).flatMap(rs -> {
            try {
                if (rs.next()) {
                    return updateOrKeep(element, rs);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            // if no element by that id, try with same group and file
            return repo.findByGroupAndFile(element.getGroup(), element.getFile()).flatMap(rs2 -> {
                try {
                    if (rs2.next()) {
                        return updateOrKeep(element, rs2);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ExternalGroupServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                return repo.add(element);
            });
        });
    }

    private Single<Long> updateOrKeep(ExternalGroup element, ResultSet rs) {
        ExternalGroup group = elementFromResultSetEntry(rs);
        // if the value stored differs from the one received
        if (!group.equals(element)) {
            return update(element);
        }
        return Single.just(0L);
    }

    @Override
    public Single<Long> update(ExternalGroup element) {
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
    public Observable<ExternalGroup> findAll() {
        return repo.findAll().flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Single<ExternalGroup> findById(long id) {
        return repo.findById(id).map(set -> {
            ExternalGroup element = null;
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

    private Observable<ExternalGroup> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscriber -> {
            try {
                while (set.next()) {
                    subscriber.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            subscriber.onCompleted();
        });
    }

    private ExternalGroup elementFromResultSetEntry(ResultSet set) {
        ExternalGroup el = new ExternalGroup();
        try {
            el.setId(set.getLong(ExternalGroup.ID));
            el.setGroup(set.getLong(ExternalGroup.GROUP));
            el.setFile(set.getString(ExternalGroup.FILE));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public Observable<ExternalGroup> findByGroup(Long id) {
        return repo.findByGroup(id).flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Observable<ExternalGroup> findByFile(String file) {
        return repo.findByFile(file).flatMapObservable(this::elementsFromResultSet);
    }

    @Override
    public Single<Long> deleteByGroup(Long id) {
        return repo.deleteByGroup(id);
    }

}
