package devs.mrp.turkeydesktop.database.imports;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;

public interface ImportFactory {
    
    Db getDb();
    DbCache<String, String> getDbCache();
    ImportService getService();
    
}
