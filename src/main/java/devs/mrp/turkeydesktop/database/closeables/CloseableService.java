/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface CloseableService {
    
    public Single<Long> add(String element);
    public Observable<Closeable> findAll();
    public Single<Closeable> findById(String id);
    public Single<Boolean> canBeClosed(String process);
    public Single<Long> deleteById(String id);
    
}
