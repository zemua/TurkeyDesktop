/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.database.titles.Title;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class TitleCondition extends RemovableLabel<Title> {

    public TitleCondition(Title element) {
        super(element);
    }
    
    @Override
    protected String getNameFromElement(Title element) {
        switch(element.getType()) {
            case POSITIVE:
                return "[+] " + element.getSubStr();
            case NEUTRAL:
                return "| |" + element.getSubStr();
            case NEGATIVE:
                return "[-] " + element.getSubStr();
            default:
                return "[?]" + element.getSubStr();
        }
    }

    @Override
    protected void initializeOtherElements() {
    }

    @Override
    protected void addOtherItems(JPanel panel) {
    }
    
}
