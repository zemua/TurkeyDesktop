package devs.mrp.turkeydesktop.database;

import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

public interface GeneralDao<VALUE, KEY> {
    
    public Single<KEY> add(VALUE element);
    
    public Single<Long> update(VALUE element);
    
    public Single<ResultSet> findAll();
    
    public Single<ResultSet> findById(KEY id);
    
    public Single<Long> deleteById(KEY id);
    
}
