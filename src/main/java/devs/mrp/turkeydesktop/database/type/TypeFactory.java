package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypeFactory {
    
    private static Supplier<DbCache<String,Type>> dbCacheSupplier;
    
    private static Supplier<Db> dbSupplier;
    
    private static Supplier<TypeDao> repoSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<String, Type>> dbCacheSupplier) {
        TypeFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<String,Type> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static void setDbSupplier(Supplier<Db> dbSupplier) {
        TypeFactory.dbSupplier = dbSupplier;
    }
    
    public static Db getDb() {
        return dbSupplier.get();
    }
    
    public static void setRepoSupplier(Supplier<TypeDao> repoSupplier) {
        TypeFactory.repoSupplier = repoSupplier;
    }
    
    public static TypeDao getTypeRepo() {
        return repoSupplier.get();
    }
    
    public static TypeService getService() {
        return new TypeServiceImpl();
    }
    
    public static Observable<Type> listFromResultSet(ResultSet set) {
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
