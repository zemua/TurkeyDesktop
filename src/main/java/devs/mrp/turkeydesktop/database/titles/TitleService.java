/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import io.reactivex.rxjava3.core.Maybe;
import java.util.Map;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface TitleService {
    
    public Single<Long> save(Title element);
    public Observable<Title> findAll();
    public Observable<Title> findContainedByAndNegativeFirst(String title);
    public Maybe<Title> findLongestContainedBy(String title);
    public Single<Title> findBySubString(String subStr);
    public Single<Long> deleteBySubString(String subStr);
    public Single<Long> countTypesOf(Title.Type type, String title);
    public Single<Map<Title.Type,Integer>> getQtyPerCategory(String title);
    
}
