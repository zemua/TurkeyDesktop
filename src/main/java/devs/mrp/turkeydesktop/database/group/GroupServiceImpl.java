/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.SingleConsumerFactory;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class GroupServiceImpl implements GroupService {
    
    private static final GroupDao repo = GroupRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupServiceImpl.class.getName());
    
    @Override
    public Observable<Long> add(Group element) {
        if (element == null) {
            return Observable.just(-1L);
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
            return Observable.just(0L);
        });
    }

    @Override
    public Observable<Long> update(Group element) {
        if (element == null) {
            return Observable.just(-1L);
        }
        return repo.update(element);
    }

    @Override
    public void findAll(Consumer<List<Group>> c) {
        Consumer<List<Group>> consumer = GroupServiceFactory.groupListConsumer(c);
        WorkerFactory.runResultSetWorker(() -> repo.findAll(), allResult -> {
            consumer.accept(elementsFromResultSet(allResult));
        });
        
    }

    @Override
    public void findById(long id, Consumer<Group> c) {
        Consumer<Group> consumer = GroupServiceFactory.groupConsumer(c);
        WorkerFactory.runResultSetWorker(() -> repo.findById(id), set -> {
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
    public void deleteById(long id, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        WorkerFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }

    @Override
    public void findAllPositive(Consumer<List<Group>> c) {
        Consumer<List<Group>> consumer = GroupServiceFactory.groupListConsumer(c);
        WorkerFactory.runResultSetWorker(() -> repo.findAllOfType(Group.GroupType.POSITIVE), allResult -> {
            consumer.accept(elementsFromResultSet(allResult));
        });
    }

    @Override
    public void findAllNegative(Consumer<List<Group>> c) {
        Consumer<List<Group>> consumer = GroupServiceFactory.groupListConsumer(c);
        WorkerFactory.runResultSetWorker(() -> repo.findAllOfType(Group.GroupType.NEGATIVE), negatives -> {
            consumer.accept(elementsFromResultSet(negatives));
        });
    }
    
    @Override
    public void setPreventClose(long groupId, boolean preventClose, IntConsumer c) {
        IntConsumer consumer = SingleConsumerFactory.getIntConsumer(c);
        WorkerFactory.runIntWorker(() -> repo.setPreventClose(groupId, preventClose), consumer);
    }
    
    @Override
    public void isPreventClose(long groupId, Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
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
