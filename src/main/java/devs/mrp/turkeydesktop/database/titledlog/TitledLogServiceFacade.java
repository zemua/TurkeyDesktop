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

/**
 *
 * @author miguel
 */
public class TitledLogServiceFacade {
    
    private ITitleService titleService = FTitleService.getService();
    private ITimeLogService logService = FTimeLogService.getService();
    
    
    
}
