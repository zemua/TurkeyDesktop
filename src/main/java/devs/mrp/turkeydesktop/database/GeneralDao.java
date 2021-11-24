/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface GeneralDao<T> {
    
    public long add(T element);
    
    public long update(T element);
    
    public ResultSet findAll();
    
    public ResultSet findById(long id);
    
    public long deleteById(long id);
    
}
