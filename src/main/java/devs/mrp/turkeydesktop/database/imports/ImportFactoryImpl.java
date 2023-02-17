package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ImportFactoryImpl implements ImportFactory {
    
    private final FactoryInitializer factory;
    private static DbCache<String, String> dbCache;
    private static ImportService importService;
    
    public ImportFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    private DbCache<String, String> buildCache() {
        return DbCacheFactory.getDbCache(new ImportsRepository(this),
            s -> s,
            key -> ImportValidator.isValidKey(key),
            this::elementsFromSet,
            (path,key) -> path);
    }
    
    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }
    
    @Override
    public DbCache<String, String> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache();
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
