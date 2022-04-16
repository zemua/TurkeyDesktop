/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacade;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class ConditionElement extends RemovableLabel<GroupConditionFacade> {

    public ConditionElement(GroupConditionFacade element) {
        super(element);
    }
    
    @Override
    protected String getNameFromElement(GroupConditionFacade facade) {
        return facade.toString();
    }

    @Override
    protected void initializeOtherElements() {
    }

    @Override
    protected void addOtherItems(JPanel panel) {
    }
    
}
