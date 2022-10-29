/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.closeables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;

/**
 *
 * @author miguel
 */
public class CloseableServiceImpl implements CloseableService {
    
    CloseableDao repo = CloseableRepository.getInstance();
    private static final Logger logger = Logger.getLogger(CloseableServiceImpl.class.getName());
    
    @Override
    public Observable<Long> add(String element) {
        if (element == null) {
            return Observable.just(-1L);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            return repo.findById(element).flatMap(rs -> {
                try{
                    if (rs.next()){
                        return Observable.just(0L);
                    } else {
                        return repo.add(new Closeable(element));
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                    return Observable.just(0L);
                }
            });
        }
    }

    @Override
    public Observable<List<Closeable>> findAll() {
        return repo.findAll().map(this::listFromResultSet);
    }

    @Override
    public Observable<Closeable> findById(String id) {
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
    public Observable<Boolean> canBeClosed(String process) {
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
    public Observable<Long> deleteById(String id) {
        return repo.deleteById(id);
    }
    
    private List<Closeable> listFromResultSet(ResultSet set) {
        List<Closeable> closeables = new ArrayList<>();
        try {
            while(set.next()) {
                closeables.add(new Closeable(set.getString(Closeable.PROCESS_NAME)));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return closeables;
    }
    
}
