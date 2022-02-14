/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.group.FGroupService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.IGroupService;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import java.util.List;

/**
 *
 * @author miguel
 */
public class ConditionChecker implements IConditionChecker {
    
    private IConditionService conditionService = FConditionService.getService();
    private IGroupService groupService = FGroupService.getService();
    private ITimeLogService timeLogService = FTimeLogService.getService();

    @Override
    public boolean isConditionMet(Condition condition) {
        // TODO
        return false;
    }

    @Override
    public boolean areConditionsMet(List<Condition> conditions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean areConditionsMet(Group group) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
