package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleFactoryImpl implements TitleFactory {
    
    private final FactoryInitializer factory;
    private final DbCache<String, Title> dbCache;
    
    public TitleFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
        this.dbCache = buildCache();
    }
    
    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }
    
    private DbCache<String, Title> buildCache() {
        return DbCacheFactory.getDbCache(new TitleRepository(this),
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            this::elementsFromResultEntry,
            (title, key) -> title);
    }
    
    @Override
    public DbCache<String, Title> getDbCache() {
        return dbCache;
    }
    
    @Override
    public TitleService getService() {
        return new TitleServiceImpl(this);
    }
    
    private Observable<Title> elementsFromResultEntry(ResultSet set) {
        return Observable.create(subscribe -> {
            try {
                while (set.next()) {
                    subscribe.onNext(titleFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscribe.onError(ex);
            }
            subscribe.onComplete();
        });
    }
    
    private Title titleFromResultSetEntry(ResultSet set) {
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
    public GroupAssignationService getGroupAssignationService() {
        return factory.getGroupAssignationFactory().getService();
    }
    
}
