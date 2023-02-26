package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;

public interface TitleFactory {
    
    Db getDb();
    DbCache<String, Title> getDbCache();
    TitleService getService();
    GroupAssignationService getGroupAssignationService();
    
}
