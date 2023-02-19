package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ImportFactoryImpl implements ImportFactory {
    
    private static ImportFactoryImpl instance;
    private static DbCache<String, String> dbCache;
    private static ImportService importService;
    
    private ImportFactoryImpl() {}
    
    public static ImportFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ImportFactoryImpl();
        }
        return instance;
    }
    
    protected DbCache<String, String> buildCache(ImportsDao repo) {
        return DbCacheFactory.getDbCache(repo,
            s -> s,
            key -> ImportValidator.isValidKey(key),
            this::elementsFromSet,
            (path,key) -> path);
    }
    
    @Override
    public Db getDb() {
        return DbFactoryImpl.getInstance().getDb();
    }
    
    @Override
    public DbCache<String, String> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new ImportsRepository(this));
        }
        return dbCache;
    }
    
    @Override
    public ImportService getService() {
        if (importService == null) {
            importService = new ImportServiceImpl(this);
        }
        return importService;
    }
    
    private Observable<String> elementsFromSet(ResultSet set) {
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
    
    private String elementFromResultSetEntry(ResultSet set) {
        try {
            return set.getString(ConfigurationEnum.IMPORT_PATH.toString());
        } catch (SQLException ex) {
            log.error("Error extracting Import from ResultSet", ex);
        }
        return StringUtils.EMPTY;
    }
}
