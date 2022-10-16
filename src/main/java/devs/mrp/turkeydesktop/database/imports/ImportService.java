/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface ImportService {
    public void add(String path, LongConsumer consumer);
    public void findAll(Consumer<List<String>> consumer);
    public void exists(String path, Consumer<Boolean> consumer);
    public void deleteById(String path, LongConsumer consumer);
}
