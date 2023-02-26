package devs.mrp.turkeydesktop.database.group.external;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;

public interface ExternalGroupFactory {
    
    public DbCache<Long,ExternalGroup> getDbCache();
    public ExternalGroupService getService();
    public Observable<ExternalGroup> elementsFromResultSet(ResultSet set);
    public Db getDb();
    
}
