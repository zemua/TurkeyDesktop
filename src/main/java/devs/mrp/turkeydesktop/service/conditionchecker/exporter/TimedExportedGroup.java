/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;

/**
 *
 * @author miguel
 */
public class TimedExportedGroup {
    
    private ExportedGroup exportedGroup;
    private long spent;

    public TimedExportedGroup() {
        
    }
    
    public TimedExportedGroup(ExportedGroup exportedGroup, long spent) {
        this.exportedGroup = exportedGroup;
        this.spent = spent;
    }

    public ExportedGroup getExportedGroup() {
        return exportedGroup;
    }

    public void setExportedGroup(ExportedGroup exportedGroup) {
        this.exportedGroup = exportedGroup;
    }

    public long getSpent() {
        return spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }
    
}
