/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.titles.*;
import java.util.function.Supplier;

/**
 *
 * @author ncm55070
 */
class FactoryInitializer {
    
    public void initialize() {
        initTitleDb();
        initTitleDbCache();
        
        initGroupAssignationService();
    }
    
    private void initTitleDb() {
        TitleFactory.setDbSupplier(dbSupplier());
    }
    
    private void initTitleDbCache() {
        TitleFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(TitleRepository.getInstance(),
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            TitleFactory::elementsFromResultEntry));
    }
    
    private void initGroupAssignationService() {
        GroupAssignationFactory.setGroupAssignationServiceSupplier(() -> new GroupAssignationService());
    }
    
    private Supplier<Db> dbSupplier() {
        return () -> Db.getInstance();
    }
    
}
