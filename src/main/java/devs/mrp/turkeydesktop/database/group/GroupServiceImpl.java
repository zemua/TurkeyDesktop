package devs.mrp.turkeydesktop.database.group;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupServiceImpl implements GroupService {
    
    private static GroupServiceImpl instance;
    private final DbCache<Long,Group> dbCache;
    
    private GroupServiceImpl(GroupFactory groupFactory) {
        dbCache = groupFactory.getDbCache();
    }
    
    public static GroupServiceImpl getInstance(GroupFactory groupFactory) {
        if (instance == null) {
            instance = new GroupServiceImpl(groupFactory);
        }
        return instance;
    }
    
    @Override
    public Single<Long> add(Group group) {
        if (GroupValidator.isInvalid(group)) {
            return Single.just(-1L);
        }
        return dbCache.save(group).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(Group element) {
        return dbCache.save(element).map(SaveAction::get);
    }

    @Override
    public Observable<Group> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<Group> findById(long id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Long> deleteById(long id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }

    @Override
    public Observable<Group> findAllPositive() {
        return dbCache.getAll().filter(g -> Group.GroupType.POSITIVE.equals(g.getType()));
    }

    @Override
    public Observable<Group> findAllNegative() {
        return dbCache.getAll().filter(g -> Group.GroupType.NEGATIVE.equals(g.getType()));
    }
    
    @Override
    public Single<Integer> setPreventClose(long groupId, boolean preventClose) {
        return dbCache.read(groupId)
                .map(g -> {
                    g.setPreventClose(preventClose);
                    return g;
                })
                .flatMapSingle(g -> dbCache.save(g))
                .map(SaveAction::get)
                .defaultIfEmpty(0L)
                .map(Long::intValue);
    }
    
    @Override
    public Single<Boolean> isPreventClose(long groupId) {
        if (doesNotBelongToAGroup(groupId)) {
            return Single.just(false);
        }
        return findById(groupId).map(group -> group != null && group.isPreventClose()).defaultIfEmpty(false);
    }
    
    private boolean doesNotBelongToAGroup(long groupId) {
        return groupId < 1;
    }
    
}
