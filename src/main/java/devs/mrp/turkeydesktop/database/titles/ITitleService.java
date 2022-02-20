/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ITitleService {
    
    public long save(Title element);
    public List<Title> findAll();
    public List<Title> findContainedByAndNegativeFirst(String title);
    public Title findLongestContainedBy(String title);
    public Title findBySubString(String subStr);
    public long deleteBySubString(String subStr);
    public long countTypesOf(Title.Type type, String title);
    
}
