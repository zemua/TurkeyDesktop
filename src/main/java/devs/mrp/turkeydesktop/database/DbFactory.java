package devs.mrp.turkeydesktop.database;

import java.util.function.Supplier;

public class DbFactory {
    
    private static Supplier<Db> dbSupplier;
    
    public static void setDbSupplier(Supplier<Db> dbSupplier) {
        DbFactory.dbSupplier = dbSupplier;
    }
    
    public static Db getDb() {
        return dbSupplier.get();
    }
    
}
