package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeService;

public interface AssignableElementFactory {
    
    public AssignableElementService getProcessesService();
    public AssignableElementService getTitlesService();
    public GroupAssignationService getAssignationService();
    public TypeService getTypeService();
    public TitleService getTitleService();
    
}
