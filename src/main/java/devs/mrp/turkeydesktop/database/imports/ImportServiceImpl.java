/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.SQLException;
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
    
    public static final DbCache<String,String> dbCache = DbCacheFactory.getDbCache(ImportsRepository.getInstance(),
            s -> s,
            ImportServiceImpl::elementsFromSet);

    @Override
    public Single<Long> add(String path) {
        return dbCache.save(path).map(SaveAction::get);
    }

    @Override
    public Observable<String> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Single<Boolean> exists(String path) {
        if (path == null || "".equals(path) || path.length() > 500) {
            return Single.just(false);
        }
        return dbCache.read(path).isEmpty().map(b -> !b);
    }

    @Override
    public Single<Long> deleteById(String path) {
        if (path == null) {
            return Single.just(-1L);
        }
        return dbCache.remove(path).map(b -> b?1L:0L);
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
