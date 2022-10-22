/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
public class GroupAssignationService implements IGroupAssignationService {
    
    private static final GroupAssignationDao repo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(GroupAssignationService.class.getName());
    
    @Override
    public void add(GroupAssignation element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1L);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            TurkeyAppFactory.runResultSetWorker(() -> repo.findByElementId(element.getType(), element.getElementId()), rs -> {
                try {
                    if (rs.next()) {
                        GroupAssignation group = elementFromResultSetEntry(rs);
                        // if the value stored differs from the one received
                        if (!group.equals(element)) {
                            update(element, consumer);
                        } else {
                            // else the value is the same as the one stored
                            consumer.accept(0L);
                        }
                    } else {
                        // else there is no element stored with this id
                        TurkeyAppFactory.runLongWorker(() -> repo.add(element), consumer::accept);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    @Override
    public void update(GroupAssignation element, LongConsumer longConsumer) {
        if (element == null) {
            longConsumer.accept(-1);
        } else {
            TurkeyAppFactory.runLongWorker(() -> repo.update(element), longConsumer::accept);
        }
    }

    @Override
    public void findAll(Consumer<List<GroupAssignation>> consumer) {
        FGroupAssignationService.runGroupAssignationListWoker(() -> elementsFromResultSet(repo.findAll()), consumer::accept);
    }

    @Deprecated
    @Override
    public void findById(long id, Consumer<GroupAssignation> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(id), result -> {
            GroupAssignation element = null;
            try {
                if (result.next()) {
                    element = elementFromResultSetEntry(result);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(element);
        });
    }

    @Deprecated
    @Override
    public void deleteById(long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteById(id), consumer::accept);
    }

    @Override
    public void findByProcessId(String processId, Consumer<GroupAssignation> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findByElementId(GroupAssignation.ElementType.PROCESS, processId), result -> {
            try {
                if (result.next()) {
                    consumer.accept(elementFromResultSetEntry(result));
                } else {
                    consumer.accept(null);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GroupAssignationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @Override
    public void deleteByProcessId(String processId, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteByElementId(GroupAssignation.ElementType.PROCESS, processId), consumer);
    }

    @Override
    public void findByTitleId(String titleId, Consumer<GroupAssignation> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findByElementId(GroupAssignation.ElementType.TITLE, titleId), set -> {
            try {
                if (set.next()) {
                    consumer.accept(elementFromResultSetEntry(set));
                } else {
                    consumer.accept(null);
                }
            } catch (SQLException ex) {
                Logger.getLogger(GroupAssignationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @Override
    public void findLongestTitleIdContainedIn(String titleId, Consumer<GroupAssignation> consumer) {
        FGroupAssignationService.runGroupAssignationListWoker(() -> elementsFromResultSet(repo.findAllOfType(GroupAssignation.ElementType.TITLE)), titleAssignations -> {
            FGroupAssignationService.runGroupAssignationWorker(() -> 
                    titleAssignations.stream()
                        .filter(ga -> StringUtils.containsIgnoreCase(titleId, ga.getElementId()))
                        .max((ga1, ga2) -> Integer.compare(ga1.getElementId().length(), ga2.getElementId().length()))
                        .orElse(null),
                    consumer);
        });
    }
    
    @Override
    public void deleteByTitleId(String titleId, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteByElementId(GroupAssignation.ElementType.TITLE, titleId), consumer);
    }

    @Override
    public void findProcessesOfGroup(Long groupId, Consumer<List<GroupAssignation>> consumer) {
        FGroupAssignationService.runGroupAssignationListWoker(() -> elementsFromResultSet(repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.PROCESS, groupId)), consumer);
    }

    @Override
    public void findTitlesOfGroup(Long groupId, Consumer<List<GroupAssignation>> consumer) {
        FGroupAssignationService.runGroupAssignationListWoker(() -> elementsFromResultSet(repo.findAllElementTypeOfGroup(GroupAssignation.ElementType.TITLE, groupId)), consumer);
    }
    
    private List<GroupAssignation> elementsFromResultSet(ResultSet set) {
        List<GroupAssignation> elements = new ArrayList<>();
        try {
            while (set.next()) {
                GroupAssignation el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return elements;
    }
    
    private GroupAssignation elementFromResultSetEntry(ResultSet set) {
        GroupAssignation el = new GroupAssignation();
        try {
            el.setType(set.getString(GroupAssignation.TYPE) != null ? GroupAssignation.ElementType.valueOf(set.getString(GroupAssignation.TYPE)) : null);
            el.setElementId(set.getString(GroupAssignation.ELEMENT_ID));
            el.setGroupId(set.getLong(GroupAssignation.GROUP_ID));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public void deleteByGroupId(long id, LongConsumer consumer) {
        TurkeyAppFactory.runLongWorker(() -> repo.deleteByGroupId(id), consumer::accept);
    }
    
}
