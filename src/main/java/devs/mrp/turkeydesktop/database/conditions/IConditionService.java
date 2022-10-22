/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface IConditionService {
    public void add(Condition element, LongConsumer consumer);
    public void update(Condition element, LongConsumer consumer);
    public void findAll(Consumer<List<Condition>> consumer);
    public void findByGroupId(Long groupId, Consumer<List<Condition>> consumer);
    public void findById(Long id, Consumer<Condition> consumer);
    public void deleteById(Long id, LongConsumer consumer);
    public void deleteByGroupId(long id, LongConsumer consumer);
    public void deleteByTargetId(long id, LongConsumer consumer);
}
