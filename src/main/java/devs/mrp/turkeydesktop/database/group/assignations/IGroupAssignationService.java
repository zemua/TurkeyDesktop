/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.assignations;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface IGroupAssignationService {
    public long add(GroupAssignation element);
    public long update(GroupAssignation element);
    public List<GroupAssignation> findAll();
    public GroupAssignation findById(long id);
    public long deleteById(long id);
    
    public GroupAssignation findByProcessId(String processId);
    public GroupAssignation findByTitleId(String titleId);
    public List<GroupAssignation> findProcessesOfGroup(Long groupId);
    public List<GroupAssignation> findTitlesOfGroup(Long groupId);
}