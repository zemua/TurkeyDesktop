package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeFactory;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class AssignableElementFactoryImpl implements AssignableElementFactory {
    
    private FactoryInitializer factory;
    
    public AssignableElementFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public AssignableElementService getProcessesService() {
        return new AssignableProcessService(this);
    }
    
    @Override
    public AssignableElementService getTitlesService() {
        return new AssignableTitleServiceImpl(this);
    }

    @Override
    public GroupAssignationService getAssignationService() {
        return factory.getGroupAssignationFactory().getService();
    }

    @Override
    public TypeService getTypeService() {
        return TypeFactory.getService();
    }

    @Override
    public TitleService getTitleService() {
        return TitleFactory.getService();
    }
    
}
