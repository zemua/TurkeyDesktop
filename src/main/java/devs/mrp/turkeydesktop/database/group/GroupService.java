/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface GroupService {
    public long add(Group element);
    public long update(Group element);
    public List<Group> findAll();
    public Group findById(long id);
    public long deleteById(long id);
    
    public List<Group> findAllPositive();
    public List<Group> findAllNegative();
    
    public int setPreventClose(long groupId, boolean preventClose);
}
