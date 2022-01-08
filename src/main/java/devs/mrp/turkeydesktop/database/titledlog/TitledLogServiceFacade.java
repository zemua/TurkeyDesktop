/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.titles.FTitleService;
import devs.mrp.turkeydesktop.database.titles.ITitleService;
import java.util.Date;
import java.util.List;

/**
 *
 * @author miguel
 */
public class TitledLogServiceFacade implements ITitledLogServiceFacade {
    
    private ITitleService titleService = FTitleService.getService();
    private ITimeLogService logService = FTimeLogService.getService();

    @Override
    public List<TitledLog> getLogsWithTitleConditions(Date from, Date to) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
