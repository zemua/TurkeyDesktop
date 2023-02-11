package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;

public interface ExportedGroupFactory {
    
    public DbCache<ExportedGroupId,ExportedGroup> getDbCache();
    public ExportedGroupService getService();
    public Observable<ExportedGroup> elementsFromResultSet(ResultSet set);
    public Db getDb();
    
}
