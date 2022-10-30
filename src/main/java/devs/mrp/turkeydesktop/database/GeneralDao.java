/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import java.sql.ResultSet;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface GeneralDao<T, I> {
    
    public Single<Long> add(T element);
    
    public Single<Long> update(T element);
    
    public Single<ResultSet> findAll();
    
    public Single<ResultSet> findById(I id);
    
    public Single<Long> deleteById(I id);
    
}
