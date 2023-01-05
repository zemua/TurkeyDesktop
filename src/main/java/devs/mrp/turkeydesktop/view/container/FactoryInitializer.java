/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package devs.mrp.turkeydesktop.view.container;

import devs.mrp.turkeydesktop.common.factory.DbCacheFactory;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationServiceImpl;
import devs.mrp.turkeydesktop.database.imports.ImportFactory;
import devs.mrp.turkeydesktop.database.imports.ImportValidator;
import devs.mrp.turkeydesktop.database.imports.ImportsRepository;
import devs.mrp.turkeydesktop.database.titles.*;
import java.util.function.Supplier;

/**
 *
 * @author ncm55070
 */
class FactoryInitializer {
    
    public void initialize() {
        initGeneralDb();
        
        initTitleDbCache();
        initImportsDbCache();
        
        initGroupAssignationService();
    }
    
    private void initGeneralDb() {
        DbFactory.setDbSupplier(dbSupplier());
    }
    
    private void initTitleDbCache() {
        TitleFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(TitleRepository.getInstance(),
            Title::getSubStr,
            key -> TitleValidator.isValidKey(key),
            TitleFactory::elementsFromResultEntry));
    }
    
    private void initImportsDbCache() {
        ImportFactory.setDbCacheSupplier(() -> DbCacheFactory.getDbCache(ImportsRepository.getInstance(),
            s -> s,
            key -> ImportValidator.isValidKey(key),
            ImportFactory::elementsFromSet));
    }
    
    private void initGroupAssignationService() {
        GroupAssignationFactory.setGroupAssignationServiceSupplier(() -> new GroupAssignationServiceImpl());
    }
    
    private Supplier<Db> dbSupplier() {
        return () -> Db.getInstance();
    }
    
}
