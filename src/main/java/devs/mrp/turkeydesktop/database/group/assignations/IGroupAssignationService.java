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
    public GroupAssignation findById(long id);
    @Deprecated
    public long deleteById(long id);
    public long deleteByGroupId(long id);
    
    public GroupAssignation findByProcessId(String processId);
    public long deleteByProcessId(String processId);
    public GroupAssignation findByTitleId(String titleId);
    public GroupAssignation findLongestTitleIdContainedIn(String titleId);
    public long deleteByTitleId(String titleId);
    public List<GroupAssignation> findProcessesOfGroup(Long groupId);
    public List<GroupAssignation> findTitlesOfGroup(Long groupId);
}
