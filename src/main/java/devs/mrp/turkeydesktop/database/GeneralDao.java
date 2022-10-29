/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface GeneralDao<T, I> {
    
    public Observable<Long> add(T element);
    
    public Observable<Long> update(T element);
    
    public Observable<ResultSet> findAll();
    
    public Observable<ResultSet> findById(I id);
    
    public Observable<Long> deleteById(I id);
    
}
