package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportedGroupFactoryImpl implements ExportedGroupFactory {
    
    private FactoryInitializer factory;
    
    public ExportedGroupFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public DbCache<ExportedGroupId,ExportedGroup> getDbCache() {
        return DbCacheFactory.getDbCache(ExportedGroupRepository.getInstance(this),
            exportedGroup -> new ExportedGroupId(exportedGroup.getGroup(), exportedGroup.getFile()),
            ExportedGroupValidator::isValidKey,
            this::elementsFromResultSet,
            (exported,id) -> exported);
    }
    
    @Override
    public ExportedGroupService getService() {
        return ExportedGroupServiceImpl.getInstance(this);
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
        return factory.getDbFactory().getDb();
    }
    
}
