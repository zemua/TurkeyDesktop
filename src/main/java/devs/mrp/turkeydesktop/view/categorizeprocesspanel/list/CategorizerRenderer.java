/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel.list;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author miguel
 */
public class CategorizerRenderer extends JPanel implements ListCellRenderer<Object> {
    
    public static Map<String, Categorizer> processCategorizers = new HashMap<>();
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> jlist, Object e, int i, boolean bln, boolean bln1) {
        // TODO
    }
    
}
