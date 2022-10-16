/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class GroupServiceImpl implements GroupService {
    
    private static final GroupDao repo = GroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupServiceImpl.class.getName());
    
    @Override
    public void add(Group element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            TurkeyAppFactory.runResultSetWorker(() -> repo.findById(element.getId()), rs -> {
                try {
                    if (rs.next()) {
                        Group group = elementFromResultSetEntry(rs);
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
    public void update(Group element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            TurkeyAppFactory.runLongWorker(() -> repo.update(element), consumer);
        }
    }

    @Override
    public void findAll(Consumer<List<Group>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findAll(), allResult -> {
            consumer.accept(elementsFromResultSet(allResult));
        });
        
    }

    @Override
    public void findById(long id, Consumer<Group> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            Group element = null;
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
    public void findAllPositive(Consumer<List<Group>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findAllOfType(Group.GroupType.POSITIVE), allResult -> {
            consumer.accept(elementsFromResultSet(allResult));
        });
    }

    @Override
    public void findAllNegative(Consumer<List<Group>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findAllOfType(Group.GroupType.NEGATIVE), negatives -> {
            consumer.accept(elementsFromResultSet(negatives));
        });
    }
    
    @Override
    public void setPreventClose(long groupId, boolean preventClose, IntConsumer consumer) {
        TurkeyAppFactory.runIntWorker(() -> repo.setPreventClose(groupId, preventClose), consumer);
    }
    
    @Override
    public void isPreventClose(long groupId, Consumer<Boolean> consumer) {
        if (groupId < 1) { // doesn't belong to a group
            consumer.accept(false);
            return;
        }
        findById(groupId, group -> {
            if (group == null) {
                consumer.accept(false);
            } else {
                consumer.accept(group.isPreventClose());
            }
        });
    }
    
    private List<Group> elementsFromResultSet(ResultSet set) {
        List<Group> elements = new ArrayList<>();
        try {
            while (set.next()) {
                Group el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
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
