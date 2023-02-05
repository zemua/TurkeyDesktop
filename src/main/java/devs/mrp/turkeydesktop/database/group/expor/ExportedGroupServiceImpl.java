package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.SaveAction;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ExportedGroupServiceImpl implements ExportedGroupService {
    
    public static final DbCache<ExportedGroupId,ExportedGroup> dbCache = ExportedGroupFactoryImpl.getDbCache();

    @Override
    public Single<Long> add(ExportedGroup exportedGroup) {
        if (ExportedGroupValidator.isInvalid(exportedGroup)) {
            log.error("Cannot save invalid exported group: " + exportedGroup);
            return Single.just(-1L);
        }
        return dbCache.save(exportedGroup).map(SaveAction::get);
    }

    @Override
    public Single<Long> update(ExportedGroup exportedGroup) {
        if (ExportedGroupValidator.isInvalid(exportedGroup)) {
            log.error("Cannot update invalid exported group: " + exportedGroup);
            return Single.just(-1L);
        }
        return dbCache.save(exportedGroup).map(SaveAction::get);
    }

    @Override
    public Observable<ExportedGroup> findAll() {
        return dbCache.getAll();
    }

    @Override
    public Maybe<ExportedGroup> findById(ExportedGroupId id) {
        return dbCache.read(id);
    }

    @Override
    public Single<Long> deleteById(ExportedGroupId id) {
        return dbCache.remove(id).map(b -> b?1L:0L);
    }

    @Override
    public Observable<ExportedGroup> findByGroup(long id) {
        return dbCache.getAll().filter(eg -> id == eg.getGroup());
    }

    @Override
    public Observable<ExportedGroup> findByFileAndGroup(long groupId, String file) {
        return dbCache.getAll().filter(eg -> groupId == eg.getGroup()).filter(eg -> StringUtils.equals(file, eg.getFile()));
    }

    @Override
    public Single<Long> deleteByGroup(long id) {
        return dbCache.getAll()
                .filter(eg -> id == eg.getGroup())
                .flatMapSingle(eg -> dbCache.remove(new ExportedGroupId(eg.getGroup(), eg.getFile())))
                .filter(Boolean::booleanValue)
                .count();
    }

}
