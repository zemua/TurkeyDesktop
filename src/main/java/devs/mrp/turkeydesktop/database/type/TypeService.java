/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface TypeService {
    
    public Single<Long> add(Type element);
    public Single<Long> update(Type element);
    public Observable<Type> findAll();
    public Maybe<Type> findById(String id);
    public Single<Long> deleteById(String id);
    public Observable<Type> findByType(Type.Types type);
    
}
