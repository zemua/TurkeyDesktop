package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;

public interface TypeFactory {
    
    public DbCache<String,Type> getDbCache();
    public Db getDb();
    public TypeService getService();
    
}
