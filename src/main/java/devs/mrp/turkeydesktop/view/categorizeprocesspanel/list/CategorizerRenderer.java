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
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> jlist, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // TODO
        String s = value.toString();
        try {
            this.setLabelText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this;
    }
    
}
