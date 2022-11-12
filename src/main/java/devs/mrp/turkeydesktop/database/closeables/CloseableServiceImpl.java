/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class CloseableServiceImpl implements CloseableService {
    
    CloseableDao repo = CloseableRepository.getInstance();
    private static final Logger logger = Logger.getLogger(CloseableServiceImpl.class.getName());
    
    @Override
    public Single<Long> add(String element) {
        if (element == null) {
            return Single.just(-1L);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            return repo.findById(element).flatMap(rs -> {
                try{
                    if (rs.next()){
                        return Single.just(0L);
                    } else {
                        return repo.add(new Closeable(element));
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    return Single.just(0L);
                }
            });
        }
    }

    @Override
    public Observable<Closeable> findAll() {
        return repo.findAll().flatMapObservable(this::listFromResultSet);
    }

    @Override
    public Single<Closeable> findById(String id) {
        return repo.findById(id).map(set -> {
            Closeable closeable = new Closeable();
            try {
                if (set.next()) {
                    closeable.setProcess(set.getString(Closeable.PROCESS_NAME));
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return closeable;
        });
    }

    @Override
    public Single<Boolean> canBeClosed(String process) {
        return repo.findById(process).map(set -> {
            try {
                return !set.next();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
                return true;
            }
        });
    }

    @Override
    public Single<Long> deleteById(String id) {
        return repo.deleteById(id);
    }
    
    private Observable<Closeable> listFromResultSet(ResultSet set) {       
        return Observable.create(suscriber -> {
            try {
                while(set.next()) {
                    suscriber.onNext(new Closeable(set.getString(Closeable.PROCESS_NAME)));
                }
            } catch (SQLException ex) {
                suscriber.onError(ex);
            }
            suscriber.onComplete();
        });
    }
    
}
