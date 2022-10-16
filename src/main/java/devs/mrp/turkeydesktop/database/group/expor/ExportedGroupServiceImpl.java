/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ExportedGroupServiceImpl implements ExportedGroupService {

    private static final ExportedGroupDao repo = ExportedGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExportedGroupServiceImpl.class.getName());

    @Override
    public void add(ExportedGroup element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            TurkeyAppFactory.runResultSetWorker(() -> repo.findById(element.getGroup()), rs -> {
                try {
                    if (rs.next()) {
                        ExportedGroup group = elementFromResultSetEntry(rs);
                        // if the value stored differs from the one received
                        if (!group.equals(element)) {
                            update(element, consumer);
                        } else {
                            // else the value is the same as the one stored
                            consumer.accept(0);
                        }
                    } else {
                        // else there is no element stored with this id
                        TurkeyAppFactory.runLongWorker(() -> repo.add(element), consumer);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public void update(ExportedGroup element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            consumer.accept(-1);
        } else {
            TurkeyAppFactory.runLongWorker(() -> repo.update(element), consumer);
        }
    }

    @Override
    public void findAll(Consumer<List<ExportedGroup>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findAll(), all -> {
            consumer.accept(elementsFromResultSet(all));
        });
    }

    @Override
    public void findById(long id, Consumer<ExportedGroup> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            ExportedGroup element = null;
            try {
                if (set.next()) {
                    element = elementFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(element);
        });
    }

    @Override
    public void deleteById(long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }

    @Override
    public void findByGroup(long id, Consumer<List<ExportedGroup>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findByGroup(id), results -> {
            consumer.accept(elementsFromResultSet(results));
        });
    }

    @Override
    public void findByFileAndGroup(long groupId, String file, Consumer<List<ExportedGroup>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findByGroupAndFile(groupId, file), set -> {
            consumer.accept(elementsFromResultSet(set));
        });
    }

    @Override
    public void deleteByGroup(long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteByGroup(id), consumer);
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
