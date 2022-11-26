/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.ResultSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
@Slf4j
public class ImportServiceImpl implements ImportService {

    private static final ImportsDao repo = ImportsRepository.getInstance();
    private static final Logger logger = Logger.getLogger(ImportServiceImpl.class.getName());
    
    public static final DbCache<String,String> dbCache = DbCacheFactory.getDbCache(ImportsRepository.getInstance(),
            s -> s,
            ImportServiceImpl::elementsFromSet);

    @Override
    public Single<Long> add(String path) {
        return exists(path).flatMap(existsResult -> {
            if (!existsResult){
                return repo.add(path);
            }
            return Single.just(0L);
        });
        
    }

    @Override
    public Observable<String> findAll() {
        return repo.findAll().flatMapObservable(set -> {
            return Observable.create(emitter -> {
                try {
                    while (set.next()) {
                        emitter.onNext(set.getString(ConfigurationEnum.IMPORT_PATH.toString()));
                    }
                } catch (SQLException ex) {
                    emitter.onError(ex);
                }
                emitter.onComplete();
            });
        });
    }

    @Override
    public Single<Boolean> exists(String path) {
        if (path == null || "".equals(path) || path.length() > 500) {
            return Single.just(false);
        }
        return repo.findById(path).map(rs -> {
            try {
                return rs.next();
            } catch (SQLException ex) {
                Logger.getLogger(ImportServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        });
    }

    @Override
    public Single<Long> deleteById(String path) {
        if (path == null) {
            return Single.just(-1L);
        }
        return repo.deleteById(path);
    }
    
    private static Observable<String> elementsFromSet(ResultSet set) {
        return Observable.create(subscribe -> {
            try {
                while (set.next()) {
                    subscribe.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscribe.onError(ex);
            }
            subscribe.onComplete();
        });
    }
    
    private static String elementFromResultSetEntry(ResultSet set) {
        try {
            return set.getString(ConfigurationEnum.IMPORT_PATH.toString());
        } catch (SQLException ex) {
            log.error("Error extracting ExportedGroup from ResultSet", ex);
        }
        return StringUtils.EMPTY;
    }

}
