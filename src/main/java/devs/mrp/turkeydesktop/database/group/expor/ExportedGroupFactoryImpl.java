package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportedGroupFactoryImpl implements ExportedGroupFactory {
    
    private static ExportedGroupFactoryImpl instance;
    private static DbCache<ExportedGroupId,ExportedGroup> dbCache;
    private static ExportedGroupService exportedGroupService;
    
    private ExportedGroupFactoryImpl(){}
    
    public static ExportedGroupFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ExportedGroupFactoryImpl();
        }
        return instance;
    }
    
    protected DbCache<ExportedGroupId,ExportedGroup> buildCache(ExportedGroupDao repo) {
        return DbCacheFactory.getDbCache(repo,
            exportedGroup -> new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile()),
            ExportedGroupValidator::isValidKey,
            this::elementsFromResultSet,
            (exported,id) -> exported);
    }
    
    @Override
    public DbCache<ExportedGroupId,ExportedGroup> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new ExportedGroupRepository(this));
        }
        return dbCache;
    }
    
    @Override
    public ExportedGroupService getService() {
        if (exportedGroupService == null) {
            exportedGroupService = new ExportedGroupServiceImpl(this);
        }
        return exportedGroupService;
    }
    
    @Override
    public Observable<ExportedGroup> elementsFromResultSet(ResultSet set) {
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

    private ExportedGroup elementFromResultSetEntry(ResultSet set) {
        ExportedGroup el = new ExportedGroup();
        try {
            el.setGroup(set.getLong(ExportedGroup.GROUP));
            el.setFile(set.getString(ExportedGroup.FILE));
            el.setDays(set.getLong(ExportedGroup.DAYS));
        } catch (SQLException ex) {
            log.error("Error extracting ExportedGroup from ResultSet", ex);
        }
        return el;
    }

    @Override
    public Db getDb() {
        return DbFactoryImpl.getInstance().getDb();
    }
    
}
