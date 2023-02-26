package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleFactoryImpl implements TitleFactory {
    
    private static TitleFactoryImpl instance;
    private static DbCache<String, Title> dbCache;
    
    protected TitleFactoryImpl() {}
    
    public static TitleFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TitleFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public Db getDb() {
        return DbFactoryImpl.getInstance().getDb();
    }
    
    protected DbCache<String, Title> buildCache(TitleDao repo) {
        return DbCacheFactory.getDbCache(repo,
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            this::elementsFromResultEntry,
            (title, key) -> title);
    }
    
    @Override
    public DbCache<String, Title> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new TitleRepository(this));
        }
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
        return GroupAssignationFactoryImpl.getInstance().getService();
    }
    
}
