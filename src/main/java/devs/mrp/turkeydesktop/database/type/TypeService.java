/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface TypeService {
    
    public long add(Type element);
    public long update(Type element);
    public List<Type> findAll();
    public Type findById(String id);
    public long deleteById(String id);
    public List<Type> findByType(Type.Types type);
    
}
