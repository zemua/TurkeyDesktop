/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.expor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface ExportedGroupService {
    public void add(ExportedGroup element, LongConsumer consumer);
    public void update(ExportedGroup element, LongConsumer consumer);
    public void findAll(Consumer<List<ExportedGroup>> consumer);
    public void findById(long id, Consumer<ExportedGroup> consumer);
    public void deleteById(long id, LongConsumer consumer);
    public void findByGroup(long id, Consumer<List<ExportedGroup>> consumer);
    public void findByFileAndGroup(long groupId, String file, Consumer<List<ExportedGroup>> consumer);
    public void deleteByGroup(long id, LongConsumer consumer);
}
