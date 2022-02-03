/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel.list;

import devs.mrp.turkeydesktop.database.type.Type;
import java.awt.Component;
import java.util.Map;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author miguel
 */
public class CategorizerRenderer extends CategorizerElement implements ListCellRenderer<Object> {
    
     /**
     * LEFT FOR REFERENCE
     * this was an element of a JList with text label and radio buttons
     * but the content of a jlist is just a render, it has no "physical" presence
     * this means the radio buttons cannot be clicked / would not fire events.
     * 
     * BETTER to layout the JButtons vertically on a JPanel using a GridLayout(0,1) or something like that
     * and then put the JPanel in a JScrollPane mocking a JList of JButtons
     */

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> jlist, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        CategorizerElement element = (CategorizerElement)value;
        
        return element;
    }
    
}
