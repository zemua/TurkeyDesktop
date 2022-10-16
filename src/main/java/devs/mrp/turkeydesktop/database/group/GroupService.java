/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface GroupService {
    public void add(Group element, LongConsumer consumer);
    public void update(Group element, LongConsumer consumer);
    public void findAll(Consumer<List<Group>> consumer);
    public void findById(long id, Consumer<Group> consumer);
    public void deleteById(long id, LongConsumer consumer);
    
    public void findAllPositive(Consumer<List<Group>> consumer);
    public void findAllNegative(Consumer<List<Group>> consumer);
    
    public void setPreventClose(long groupId, boolean preventClose, IntConsumer consumer);
    public void isPreventClose(long groupId, Consumer<Boolean> consumer);
}
