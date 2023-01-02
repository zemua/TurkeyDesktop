/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import io.reactivex.rxjava3.core.Maybe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class TitleServiceImpl implements TitleService {

    public static final DbCache<String,Title> dbCache = DbCacheFactory.getDbCache(TitleRepository.getInstance(),
            Title::getSubStr,
            key -> isValidKey(key),
            TitleServiceImpl::elementsFromResultEntry);
    private static final IGroupAssignationService assignationService = FGroupAssignationService.getService();
    
    private static boolean isValidKey(String titleSubString) {
        return titleSubString != null && !titleSubString.isEmpty();
    }

    @Override
    public Single<Long> save(Title element) {
        if (element == null) {
            return Single.just(-1L);
        }
        element.setSubStr(element.getSubStr().toLowerCase());
        return dbCache.save(element).map(SaveAction::get);
    }

    private Single<Long> update(Title element) {
        if (element == null || element.getSubStr() == null) {
            return Single.just(-1L);
        }
        element.setSubStr(element.getSubStr().toLowerCase());
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<Title> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<Title> findBySubString(String subStr) {
        String lowerCaseSubStr = subStr.toLowerCase();
        return dbCache.read(lowerCaseSubStr);
    }

    @Override
    public Single<Long> deleteBySubString(String subStr) {
        if (subStr == null) {
            return Single.just(-1L);
        }
        return Single.zip(dbCache.remove(subStr).map(b -> b?1L:0L),
                assignationService.deleteByTitleId(subStr),
                (r1,r2) ->{
                    return r1;
                });
    }
    
    private static Observable<Title> elementsFromResultEntry(ResultSet set) {
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

    private static Title elementFromResultSetEntry(ResultSet set) {
        Title el = new Title();
        try {
            el.setSubStr(set.getString(Title.SUB_STR).toLowerCase());
            el.setType(Title.Type.valueOf(set.getString(Title.TYPE)));
        } catch (SQLException ex) {
            log.error("Error transforming Title from ResultSet entry", ex);
        }
        return el;
    }

    @Override
    public Observable<Title> findContainedByAndNegativeFirst(String title) {
        return dbCache.getAll()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getSubStr()))
                .sorted((e1, e2) -> e2.getType().compareTo(e1.getType())); // "NEGATIVE" comes before "POSITIVE" in natural order
    }
    
    @Override
    public Maybe<Title> findLongestContainedBy(String title) {
        return dbCache.getAll()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getSubStr()))
                .reduce((e1,e2) -> {
                    if (e1.getSubStr().length() > e2.getSubStr().length()) {
                        return e1;
                    }
                    if (e2.getSubStr().length() > e1.getSubStr().length()) {
                        return e2;
                    }
                    if (e1.getType().equals(e2.getType())) {
                        return e1;
                    }
                    if (e1.getType().equals(Title.Type.NEGATIVE)) {
                        return e1;
                    }
                    if (e2.getType().equals(Title.Type.NEGATIVE)) {
                        return e2;
                    }
                    if (e1.getType().equals(Title.Type.NEUTRAL)) {
                        return e1;
                    }
                    if (e2.getType().equals(Title.Type.NEUTRAL)) {
                        return e2;
                    }
                    // when same length and both are POSITIVE, then default to the first one
                    return e1;
                });
    }

    @Override
    public Single<Long> countTypesOf(Title.Type type, String title) {
        return dbCache.getAll().filter(e -> StringUtils.containsIgnoreCase(title, e.getSubStr())).count();
    }
    
    private class TypeQty {
        Title.Type type;
        Long qty;
        TypeQty(Title.Type t, Long q) {
            this.type = t;
            this.qty = q;
        }
    }

    @Override
    public Single<Map<Title.Type, Integer>> getQtyPerCategory(String title) {
        log.debug("Getting quantities for title: {}", title);
        return dbCache.getAll()
                .filter(t -> !t.getSubStr().isBlank())
                .filter(t -> StringUtils.containsIgnoreCase(title, t.getSubStr()))
                .groupBy(t -> t.getType())
                .flatMapSingle(groupedObs -> groupedObs.toList())
                .map(list -> {
                    log.debug("grouped object list: {}", list);
                    Title.Type type = list.get(0).getType();
                    return new TypeQty(type,Long.valueOf(list.size()));
                })
                .toMap(tq -> tq.type, tq -> tq.qty.intValue())
                .toMaybe().defaultIfEmpty(Collections.EMPTY_MAP);
    }

}
