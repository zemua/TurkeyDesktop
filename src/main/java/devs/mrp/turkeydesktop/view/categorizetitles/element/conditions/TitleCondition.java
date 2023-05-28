package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.database.titles.Title;
import javax.swing.JPanel;

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
    protected void initializeOtherElements(Title element) {
    }

    @Override
    protected void addOtherItems(JPanel panel, Title element) {
    }
    
}
