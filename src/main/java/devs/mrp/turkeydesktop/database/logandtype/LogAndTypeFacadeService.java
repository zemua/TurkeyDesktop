/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogRepository;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.FTitleService;
import devs.mrp.turkeydesktop.database.titles.ITitleService;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.FTypeService;
import devs.mrp.turkeydesktop.database.type.ITypeService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeRepository;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
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
    private final TimeLogRepository logRepo = TimeLogRepository.getInstance();
    private final TypeRepository typeRepo = TypeRepository.getInstance();
    private final ITimeLogService logService = FTimeLogService.getService();
    private final ITypeService typeService = FTypeService.getService();
    private final IConfigElementService configService = FConfigElementService.getService();
    private final ITitleService titleService = FTitleService.getService();
    
    private static final Logger LOGGER = Logger.getLogger(LogAndTypeFacadeService.class.getName());

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
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return typedTimes;
    }

    @Override
    public long addTimeLogAdjustingCounted(TimeLog element) {
        adjustCounted(element);
        adjustAccumulated(element, element.getCounted());
        adjustGroup(element);
        return logService.add(element);
    }
    
    private TimeLog adjustCounted(TimeLog element) {
        Type type = typeService.findById(element.getProcessName());
        if (type == null || type.getType() == null) {
            type = new Type();
            type.setType(Type.Types.UNDEFINED);
        }
        switch (type.getType()){
            case NEUTRAL:
                element.setCounted(0);
                element.setType(Type.Types.NEUTRAL);
                break;
            case UNDEFINED:
                element.setCounted(0);
                element.setType(Type.Types.UNDEFINED);
                break;
            case DEPENDS:
                setCountedDependingOnTitle(element, element.getElapsed());
                element.setType(Type.Types.DEPENDS);
                break;
            case POSITIVE:
                element.setCounted(Math.abs(element.getElapsed()));
                element.setType(Type.Types.POSITIVE);
                break;
            case NEGATIVE:
                int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
                element.setCounted(Math.abs(element.getElapsed()) * proportion * (-1));
                element.setType(Type.Types.NEGATIVE);
                break;
            default:
                break;
        }
        return element;
    }
    
    private void adjustGroup(TimeLog element) {
        // TODO set the group before saving the entry in the log
    }
    
    private TimeLog setCountedDependingOnTitle(TimeLog element, long elapsed) {
        var list = titleService.findContainedByAndNegativeFirst(element.getWindowTitle());
        Title.Type type;
        if (list.size() > 0) {
            type = list.get(0).getType();
        } else {
            element.setCounted(0);
            return element;
        }
        if (type.equals(Title.Type.POSITIVE)) {
            element.setCounted(Math.abs(elapsed));
        } else {
            element.setCounted(- Math.abs(elapsed));
        }
        return element;
    }
    
    private TimeLog adjustAccumulated(TimeLog element, long counted) {
        TimeLog last = logService.findMostRecent();
        long lastAccumulated = 0;
        if (last != null) {
            lastAccumulated = last.getAccumulated();
        }
        long accumulated = lastAccumulated + counted;
        element.setAccumulated(accumulated);
        //LOGGER.log(Level.INFO, "accumulated: " + accumulated);
        return element;
    }

}
