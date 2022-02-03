/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.group.facade;

import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.titles.FTitleService;
import devs.mrp.turkeydesktop.database.titles.ITitleService;
import java.util.List;

/**
 *
 * @author miguel
 */
public class AssignableTitleService implements IAssignableElementService {
    
    private ITitleService titleService = FTitleService.getService();
    private IGroupAssignationService assignationService = FGroupAssignationService.getService();
    
    @Override
    public List<AssignableElement> positiveElementsWithAssignation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AssignableElement> negativeElementsWithAssignation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
