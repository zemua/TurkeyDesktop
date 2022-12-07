/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.common.factory;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.impl.DbCacheImpl;
import devs.mrp.turkeydesktop.database.GeneralDao;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.util.function.Function;

/**
 *
 * @author ncm55070
 */
public class DbCacheFactory {
    public static <KEY,VALUE> DbCache getDbCache(GeneralDao<VALUE, KEY> repo,
            Function<VALUE,KEY> keyExtractor,
            Function<ResultSet,Observable<VALUE>> listFromResultSet) {
        return new DbCacheImpl<>(repo, keyExtractor, listFromResultSet);
    }
}
