/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface TitleService {
    
    public void save(Title element, LongConsumer consumer);
    public void findAll(Consumer<List<Title>> consumer);
    public void findContainedByAndNegativeFirst(String title, Consumer<List<Title>> consumer);
    public void findLongestContainedBy(String title, Consumer<Title> consumer);
    public void findBySubString(String subStr, Consumer<Title> consumer);
    public void deleteBySubString(String subStr, LongConsumer consumer);
    public void countTypesOf(Title.Type type, String title, LongConsumer consumer);
    
}
