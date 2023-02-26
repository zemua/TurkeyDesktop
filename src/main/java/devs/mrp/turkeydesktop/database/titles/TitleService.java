package devs.mrp.turkeydesktop.database.titles;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;

public interface TitleService {
    
    public Single<Long> save(Title element);
    public Observable<Title> findAll();
    public Observable<Title> findContainedByAndNegativeFirst(String title);
    public Maybe<Title> findLongestContainedBy(String title);
    public Maybe<Title> findBySubString(String subStr);
    public Single<Long> deleteBySubString(String subStr);
    public Single<Long> countTypesOf(Title.Type type, String title);
    public Single<Map<Title.Type,Integer>> getQtyPerCategory(String title);
    
}
