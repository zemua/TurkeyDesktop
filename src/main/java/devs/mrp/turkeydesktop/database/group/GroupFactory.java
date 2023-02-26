package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;

public interface GroupFactory {
    
    public DbCache<Long, Group> getDbCache();
    public GroupService getService();
    public Observable<Group> elementsFromResultSet(ResultSet set);
    public Db getDb();
    
}
