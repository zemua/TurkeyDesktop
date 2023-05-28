package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;

public interface TypeFactory {
    
    public DbCache<String,Type> getDbCache();
    public Db getDb();
    public TypeService getService();
    public GroupAssignationService getGroupAssignationService();
    
}
