/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.titles.FTitleService;
import devs.mrp.turkeydesktop.database.titles.ITitleService;
import devs.mrp.turkeydesktop.database.titles.Title;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author miguel
 */
public class TitledLogServiceFacade implements ITitledLogServiceFacade {
    
    private ITitleService titleService = FTitleService.getService();
    private ITimeLogService logService = FTimeLogService.getService();

    @Override
    public List<TitledLog> getLogsWithTitleConditions(Date from, Date to) {
        return logService.logsGroupedByTitle(from, to).stream()
                .map(e -> {
                    TitledLog tl = new TitledLog();
                    tl.setTitle(e.getValue1());
                    tl.setElapsed(e.getValue2());
                    tl.setConditions(titleService.findContainedBy(e.getValue1()));
                    tl.setQtyPositives(titleService.countTypesOf(Title.Type.POSITIVE, e.getValue1()));
                    tl.setQtyNegatives(titleService.countTypesOf(Title.Type.NEGATIVE, e.getValue1()));
                    return tl;
                })
                .collect(Collectors.toList());
    }
    
}
