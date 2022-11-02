/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public class TitleServiceImpl implements TitleService {

    private final TitleDao repo = TitleRepository.getInstance();
    private static final GroupAssignationDao assignationRepo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TitleServiceImpl.class.getName());

    private static Map<String, Title> conditionsMap;

    public TitleServiceImpl() {
        initConditionsMap();
    }

    private void initConditionsMap() {
        if (conditionsMap == null) {
            conditionsMap = new ConcurrentHashMap<>();
            retrieveAll().subscribe(); // it assigns values to the hashmap inside the function
        }
    }

    @Override
    public Single<Long> save(Title element) {
        if (element == null) {
            return Single.just(-1L);
        } else {
            element.setSubStr(element.getSubStr().toLowerCase());
            if (conditionsMap.containsKey(element.getSubStr())) {
                if (conditionsMap.get(element.getSubStr()).getType() != element.getType()) {
                    // we have this value but is different, so we update
                    conditionsMap.put(element.getSubStr(), element);
                    return update(element);
                } // else the value is the same as the one stored
                return Single.just(0L);
            } // we don't have this value so we add new
            conditionsMap.put(element.getSubStr(), element);
            return repo.add(element);
        }
    }

    private Single<Long> update(Title element) {
        if (element == null || element.getSubStr() == null) {
            return Single.just(-1L);
        } else {
            element.setSubStr(element.getSubStr().toLowerCase());
            conditionsMap.put(element.getSubStr(), element);
            return repo.update(element);
        }
    }

    private Observable<Title> retrieveAll() {
        conditionsMap.clear();
        return repo.findAll().flatMapObservable(set -> {
            return Observable.create(submitter -> {
                try {
                    while (set.next()) {
                        Title el = elementFromResultSetEntry(set);
                        submitter.onNext(el);
                        conditionsMap.put(el.getSubStr(), el);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                submitter.onCompleted();
            });
        });
    }

    @Override
    public Observable<Title> findAll() {
        return Observable.create(emitter -> {
            conditionsMap.entrySet().stream()
                .map(e -> {
                    Title el = new Title();
                    el.setSubStr(e.getKey());
                    el.setType(e.getValue().getType());
                    return el;
                }).forEach(title -> emitter.onNext(title));
            emitter.onCompleted();
        });
    }

    @Override
    public Single<Title> findBySubString(String subStr) {
        String lowerCaseSubStr = subStr.toLowerCase();
        if (conditionsMap.containsKey(lowerCaseSubStr)) {
            Title el = new Title();
            el.setSubStr(lowerCaseSubStr);
            el.setType(conditionsMap.get(lowerCaseSubStr).getType());
            return Single.just(el);
        }
        return Single.just(null);
    }

    @Override
    public Single<Long> deleteBySubString(String subStr) {
        if (subStr == null) {
            return Single.just(-1L);
        } else {
            String lowerCased = subStr.toLowerCase();
            conditionsMap.remove(lowerCased);
            return assignationRepo.deleteByElementId(GroupAssignation.ElementType.TITLE, lowerCased)
                    .flatMap(r -> repo.deleteById(lowerCased));
        }
    }

    private Title elementFromResultSetEntry(ResultSet set) {
        Title el = new Title();
        try {
            el.setSubStr(set.getString(Title.SUB_STR).toLowerCase());
            el.setType(Title.Type.valueOf(set.getString(Title.TYPE)));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public Observable<Title> findContainedByAndNegativeFirst(String title) {
        return Observable.create(subscriber -> {
            conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e2.getType().compareTo(e1.getType())) // "NEGATIVE" before "POSITIVE" in natural order
                .forEach(e -> subscriber.onNext(e));
            subscriber.onCompleted();
        });
    }
    
    @Override
    public Single<Title> findLongestContainedBy(String title) {
        return Single.create(subscriber -> {
            var result = conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .max((e1, e2) -> Long.compare(e1.getKey().length(), e2.getKey().length()))
                .map(e -> e.getValue())
                .orElse(null);
            subscriber.onSuccess(result);
        });
    }

    @Override
    public Single<Long> countTypesOf(Title.Type type, String title) {
        return Single.create(subscriber -> {
            var count = conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .filter(e -> e.getValue().getType().equals(type))
                .count();
            subscriber.onSuccess(count);
        });
    }

    @Override
    public Single<Map<Title.Type, Integer>> getQtyPerCategory(String title) {
        return Single.create(subscriber -> {
            Map<Title.Type, Integer> map = new HashMap<>();
            Title.Type[] types = Title.Type.values();
            for (Title.Type type : types) {
                map.put(type, 0);
            }
            conditionsMap.entrySet().stream()
                    .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                    .forEach(e -> map.put(e.getValue().getType(), map.get(e.getValue().getType())+1));
            subscriber.onSuccess(map);
        });
    }

}
