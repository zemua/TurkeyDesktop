/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database;

import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface GeneralDao<VALUE, KEY> {
    
    public Single<KEY> add(VALUE element);
    
    public Single<Long> update(VALUE element);
    
    public Single<ResultSet> findAll();
    
    public Single<ResultSet> findById(KEY id);
    
    public Single<Long> deleteById(KEY id);
    
}
