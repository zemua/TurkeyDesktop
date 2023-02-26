package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.titles.TitleFactoryImpl;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeFactoryImpl;
import devs.mrp.turkeydesktop.database.type.TypeService;

public class AssignableElementFactoryImpl implements AssignableElementFactory {
    
    private static AssignableElementFactoryImpl instance;
    
    private AssignableElementFactoryImpl(){}
    
    public static AssignableElementFactoryImpl getInstance() {
        if (instance == null) {
            instance = new AssignableElementFactoryImpl();
        }
        return instance;
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
        return GroupAssignationFactoryImpl.getInstance().getService();
    }

    @Override
    public TypeService getTypeService() {
        return TypeFactoryImpl.getInstance().getService();
    }

    @Override
    public TitleService getTitleService() {
        return TitleFactoryImpl.getInstance().getService();
    }
    
}
