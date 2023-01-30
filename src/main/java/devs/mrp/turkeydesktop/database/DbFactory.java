package devs.mrp.turkeydesktop.database;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbFactory {
    
    private static Supplier<Db> dbSupplier;
    
    public static void setDbSupplier(Supplier<Db> dbSupplier) {
        log.debug("Setting db supplier to {}", dbSupplier);
        DbFactory.dbSupplier = dbSupplier;
    }
    
    public static Db getDb() {
        Db db = dbSupplier.get();
        log.debug("Getting db {}", db);
        return db;
    }
    
}
