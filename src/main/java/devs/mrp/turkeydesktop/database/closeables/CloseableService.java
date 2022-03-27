/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface CloseableService {
    
    public long add(Closeable element);
    public List<Closeable> findAll();
    public Closeable findById(String id);
    public boolean canBeClosed(String process);
    public long deleteById(String id);
    
}
