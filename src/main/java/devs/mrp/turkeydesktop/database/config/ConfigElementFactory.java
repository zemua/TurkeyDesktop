package devs.mrp.turkeydesktop.database.config;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ConfigElementFactory {
    
    public void setDbCacheSupplier(Supplier<DbCache<String, ConfigElement>> dbCacheSupplier);
    
    public DbCache<String, ConfigElement> getDbCache();

    public void setDbSupplier(Supplier<Db> dbSupplier);
    
    public Db getDb();

    public void setRepoSupplier(Supplier<ConfigElementDao> repoSupplier);
    
    public ConfigElementDao getRepo();
    
    public ConfigElementService getService();
    
    public void runConditionListWorker(Supplier<List<ConfigElement>> supplier, Consumer<List<ConfigElement>> consumer);
    
    public void runConditionWorker(Supplier<ConfigElement> supplier, Consumer<ConfigElement> consumer);
    
    public Observable<ConfigElement> elementsFromResultSet(ResultSet set);

    public ConfigElement elementFromResultSetEntry(ResultSet set);
    
}
