package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;

public interface GroupAssignationFactory {
    
    public GroupAssignationService getService();
    public DbCache<GroupAssignationDao.ElementId, GroupAssignation> getDbCache();
    public Observable<GroupAssignation> elementsFromResultSet(ResultSet set);
    Db getDb();
    
}
