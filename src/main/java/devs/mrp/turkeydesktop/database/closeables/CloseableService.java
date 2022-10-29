/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import java.util.List;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface CloseableService {
    
    public Observable<Long> add(String element);
    public Observable<List<Closeable>> findAll();
    public Observable<Closeable> findById(String id);
    public Observable<Boolean> canBeClosed(String process);
    public Observable<Long> deleteById(String id);
    
}
