/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface IGroupAssignationService {
    public void add(GroupAssignation element, LongConsumer consumer);
    public void update(GroupAssignation element, LongConsumer consumer);
    public void findAll(Consumer<List<GroupAssignation>> consumer);
    @Deprecated
    public void findById(long id, Consumer<GroupAssignation> consumer);
    @Deprecated
    public void deleteById(long id, LongConsumer consumer);
    public void deleteByGroupId(long id, LongConsumer consumer);
    
    public void findByProcessId(String processId, Consumer<GroupAssignation> consumer);
    public long deleteByProcessId(String processId);
    public GroupAssignation findByTitleId(String titleId);
    public GroupAssignation findLongestTitleIdContainedIn(String titleId);
    public long deleteByTitleId(String titleId);
    public List<GroupAssignation> findProcessesOfGroup(Long groupId);
    public List<GroupAssignation> findTitlesOfGroup(Long groupId);
}
