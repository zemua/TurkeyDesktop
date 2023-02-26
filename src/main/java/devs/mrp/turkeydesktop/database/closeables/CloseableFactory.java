package devs.mrp.turkeydesktop.database.closeables;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;

public interface CloseableFactory {
    public DbCache<String, Closeable> getDbCache();
    public CloseableService getService();
    public Observable<Closeable> listFromResultSet(ResultSet set);
    public Db getDb();
}
