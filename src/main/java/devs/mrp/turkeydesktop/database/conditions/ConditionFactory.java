package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;

public interface ConditionFactory {
    public Db getDb();
    public DbCache<Long, Condition> getDbCache();
    public ConditionService getService();
    public Observable<Condition> elementsFromResultSet(ResultSet set);
}
