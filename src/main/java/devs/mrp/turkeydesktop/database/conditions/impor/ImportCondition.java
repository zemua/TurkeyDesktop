/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions.impor;

import devs.mrp.turkeydesktop.database.conditions.AbstractCondition;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import java.util.Objects;

/**
 *
 * @author miguel
 */
public class ImportCondition extends AbstractCondition<ImportCondition> {
    
    public static final String TXT_FILE = "TXT_FILE";
    
    private String txtFile;

    public String getTxtFile() {
        return txtFile;
    }

    public void setTxtFile(String txtFile) {
        this.txtFile = txtFile;
    }

    @Override
    protected boolean otherFieldsEquals(ImportCondition condition) {
        return !Objects.isNull(condition.getTxtFile()) ? condition.getTxtFile().equals(txtFile) : Objects.isNull(txtFile);
    }
    
}
