/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.external;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface ExternalGroupService {
    public void add(ExternalGroup element, LongConsumer consumer);
    public void update(ExternalGroup element, LongConsumer consumer);
    public void findAll(Consumer<List<ExternalGroup>> consumer);
    public void findById(long id, Consumer<ExternalGroup> consumer);
    public void deleteById(long id, LongConsumer consumer);
    public void findByGroup(Long id, Consumer<List<ExternalGroup>> consumer);
    public void findByFile(String file, Consumer<List<ExternalGroup>> consumer);
    public void deleteByGroup(Long id, LongConsumer consumer);
}
