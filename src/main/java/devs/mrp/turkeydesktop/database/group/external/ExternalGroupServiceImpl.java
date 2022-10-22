/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.WorkerFactory;
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
public class ExternalGroupServiceImpl implements ExternalGroupService {

    private static final ExternalGroupDao repo = ExternalGroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ExternalGroupServiceImpl.class.getName());

    @Override
    public void add(ExternalGroup element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            WorkerFactory.runResultSetWorker(() -> repo.findById(element.getId()), rs -> {
                try {
                    if (rs.next()) {
                        updateOrKeep(element, rs, consumer);
                    } else {
                        // if no element by that id, try with same group and file
                        WorkerFactory.runResultSetWorker(() -> repo.findByGroupAndFile(element.getGroup(), element.getFile()), rs2 -> {
                            try {
                                if (rs2.next()) {
                                    updateOrKeep(element, rs2, consumer);
                                } else {
                                    // else there is no element stored with this id
                                    WorkerFactory.runLongWorker(() -> repo.add(element), consumer);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(ExternalGroupServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    private void updateOrKeep(ExternalGroup element, ResultSet rs, LongConsumer consumer) {
        ExternalGroup group = elementFromResultSetEntry(rs);
        // if the value stored differs from the one received
        if (!group.equals(element)) {
            update(element, consumer);
        } else {
            // else the value is the same as the one stored
            consumer.accept(0);
        }
        
    }

    @Override
    public void update(ExternalGroup element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else if (element.getFile() != null && element.getFile().length() > 500) {
            logger.log(Level.SEVERE, "File path cannot be longer than 500 characters");
            consumer.accept(-1);
        } else {
            WorkerFactory.runLongWorker(() -> repo.update(element), consumer);
        }
    }

    @Override
    public void findAll(Consumer<List<ExternalGroup>> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findAll(), rs -> {
            consumer.accept(elementsFromResultSet(rs));
        });
    }

    @Override
    public void findById(long id, Consumer<ExternalGroup> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            ExternalGroup element = null;
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
        WorkerFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }

    private List<ExternalGroup> elementsFromResultSet(ResultSet set) {
        List<ExternalGroup> elements = new ArrayList<>();
        try {
            while (set.next()) {
                ExternalGroup el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
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
    public void findByGroup(Long id, Consumer<List<ExternalGroup>> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findByGroup(id), rs -> {
            consumer.accept(elementsFromResultSet(rs));
        });
    }

    @Override
    public void findByFile(String file, Consumer<List<ExternalGroup>> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findByFile(file), rs -> {
            consumer.accept(elementsFromResultSet(rs));
        });
    }

    @Override
    public void deleteByGroup(Long id, LongConsumer consumer) {
        WorkerFactory.runLongWorker(() -> repo.deleteByGroup(id), consumer);
    }

}
