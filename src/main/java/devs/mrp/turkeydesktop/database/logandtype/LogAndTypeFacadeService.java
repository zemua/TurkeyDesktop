/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.type.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class LogAndTypeFacadeService implements ILogAndTypeService {
    
    private final LogAndTypeFacadeDao repo = LogAndTypeFacadeRepository.getInstance();

    @Override
    public List<Tripla<String, Long, Type.Types>> getTypedLogGroupedByProcess(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        List<Tripla<String, Long, Type.Types>> typedTimes = new ArrayList<>();
        ResultSet set = repo.getTypedLogGroupedByProcess(fromMilis, toMilis);
        try {
            while (set.next()) {
                Tripla<String, Long, Type.Types> tripla = new Tripla<>();
                tripla.setValue1(set.getString(TimeLog.PROCESS_NAME));
                tripla.setValue2(set.getLong(3));
                String type = set.getString(Type.TYPE);
                tripla.setValue3(type != null ? Type.Types.valueOf(type) : Type.Types.UNDEFINED);
                typedTimes.add(tripla);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimeLogService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return typedTimes;
    }
    
}
