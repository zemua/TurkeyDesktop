package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeFactoryImpl implements TypeFactory {
    
    private static TypeFactoryImpl instance;
    private static DbCache<String,Type> dbCache;
    private static TypeService typeService;
    
    private TypeFactoryImpl() {}
    
    public static TypeFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TypeFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public DbCache<String,Type> getDbCache() {
        if (dbCache == null) {
            dbCache = buildCache(new TypeRepository(this));
        }
        return dbCache;
    }
    
    @Override
    public Db getDb() {
        return DbFactoryImpl.getInstance().getDb();
    }
    
    @Override
    public TypeService getService() {
        if (typeService == null) {
            typeService = new TypeServiceImpl(this);
        }
        return typeService;
    }
    
    protected DbCache<String,Type> buildCache(TypeDao repo) {
        return DbCacheFactory.getDbCache(repo,
            type -> type.getProcess(),
            TypeValidator::isValidKey,
            this::listFromResultSet,
            (type,key) -> type);
    }
    
    private Observable<Type> listFromResultSet(ResultSet set) {
        return Observable.create(submitter -> {
            try {
                while (set.next()) {
                    Type type = new Type();
                    type.setProcess(set.getString(Type.PROCESS_NAME));
                    type.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
                    submitter.onNext(type);
                }
            } catch (SQLException ex) {
                log.error("error creating observable from resultSet", ex);
                submitter.onError(ex);
            }
            submitter.onComplete();
        });
    }
    
}
