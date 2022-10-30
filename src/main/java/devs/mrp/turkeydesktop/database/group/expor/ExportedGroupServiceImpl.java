/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class ExportedGroupServiceImpl implements ExportedGroupService {

    private static final ExportedGroupDao repo = ExportedGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExportedGroupServiceImpl.class.getName());

    @Override
    public Observable<Long> add(ExportedGroup element) {
        if (element == null) {
            return Observable.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return Observable.just(-1L);
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
            return Observable.just(0L);
        });
    }

    @Override
    public Observable<Long> update(ExportedGroup element) {
        if (element == null) {
            return Observable.just(-1L);
        }
        if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            return Observable.just(-1L);
        }
        return repo.update(element);
    }

    @Override
    public Observable<List<ExportedGroup>> findAll() {
        return repo.findAll().map(this::elementsFromResultSet);
    }

    @Override
    public Observable<ExportedGroup> findById(long id) {
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
    public Observable<Long> deleteById(long id) {
        return repo.deleteById(id);
    }

    @Override
    public Observable<List<ExportedGroup>> findByGroup(long id) {
        return repo.findByGroup(id).map(this::elementsFromResultSet);
    }

    @Override
    public Observable<List<ExportedGroup>> findByFileAndGroup(long groupId, String file) {
        return repo.findByGroupAndFile(groupId, file).map(this::elementsFromResultSet);
    }

    @Override
    public Observable<Long> deleteByGroup(long id) {
        return repo.deleteByGroup(id);
    }

    private List<ExportedGroup> elementsFromResultSet(ResultSet set) {
        List<ExportedGroup> elements = new ArrayList<>();
        try {
            while (set.next()) {
                ExportedGroup el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
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
