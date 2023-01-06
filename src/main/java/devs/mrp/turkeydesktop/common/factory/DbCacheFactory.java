package devs.mrp.turkeydesktop.common.factory;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.impl.DbCacheImpl;
import devs.mrp.turkeydesktop.database.GeneralDao;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.util.function.Function;

public class DbCacheFactory {
    public static <KEY,VALUE> DbCache getDbCache(GeneralDao<VALUE, KEY> repo,
            Function<VALUE,KEY> keyExtractor,
            Function<KEY,Boolean> isNewKey,
            Function<ResultSet,Observable<VALUE>> listFromResultSet) {
        return new DbCacheImpl<>(repo, keyExtractor, isNewKey, listFromResultSet);
    }
}
