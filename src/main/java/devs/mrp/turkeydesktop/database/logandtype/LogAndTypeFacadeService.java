/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.group.FGroupService;
import devs.mrp.turkeydesktop.database.group.IGroupService;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogRepository;
import devs.mrp.turkeydesktop.database.titles.FTitleService;
import devs.mrp.turkeydesktop.database.titles.ITitleService;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.FTypeService;
import devs.mrp.turkeydesktop.database.type.ITypeService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeRepository;
import devs.mrp.turkeydesktop.service.conditionchecker.FConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.IConditionChecker;
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
    private final IGroupAssignationService groupAssignationService = FGroupAssignationService.getService();
    private final IConditionService conditionService = FConditionService.getService();
    
    private final IConditionChecker conditionChecker = FConditionChecker.getConditionChecker();
    
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
    public TimeLog addTimeLogAdjustingCounted(TimeLog element) {
        adjustDependingOnType(element);
        adjustAccumulated(element, element.getCounted());
        logService.add(element);
        return element;
    }
    
    private TimeLog adjustDependingOnType(TimeLog element) {
        Type type = typeService.findById(element.getProcessName());
        if (type == null || type.getType() == null) {
            type = new Type();
            type.setType(Type.Types.UNDEFINED);
        }
        switch (type.getType()){
            case NEUTRAL:
                element.setType(Type.Types.NEUTRAL);
                element.setGroupId(-1);
                element.setCounted(0);
                break;
            case UNDEFINED:
                element.setType(Type.Types.UNDEFINED);
                element.setGroupId(-1);
                element.setCounted(0);
                break;
            case DEPENDS:
                element.setType(Type.Types.DEPENDS);
                // If title is "hello to you" and we have records "hello" in group1 and "hello to" in group2 the group2 will be chosen
                GroupAssignation assignation = groupAssignationService.findLongestTitleIdContainedIn(element.getWindowTitle());
                element.setGroupId(assignation != null ? assignation.getGroupId() : -1);
                setCountedDependingOnTitle(element, element.getElapsed());
                break;
            case POSITIVE:
                element.setType(Type.Types.POSITIVE);
                GroupAssignation positiveAssignation = groupAssignationService.findByProcessId(element.getProcessName());
                element.setGroupId(positiveAssignation != null ? positiveAssignation.getGroupId() : -1);
                element.setCounted(conditionChecker.areConditionsMet(element.getGroupId()) ? Math.abs(element.getElapsed()) : 0);
                break;
            case NEGATIVE:
                int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
                element.setType(Type.Types.NEGATIVE);
                GroupAssignation negativeAssignation = groupAssignationService.findByProcessId(element.getProcessName());
                element.setGroupId(negativeAssignation != null ? negativeAssignation.getGroupId() : -1);
                element.setCounted(Math.abs(element.getElapsed()) * proportion * (-1));
                break;
            default:
                break;
        }
        return element;
    }
    
    private TimeLog setCountedDependingOnTitle(TimeLog element, long elapsed) {
        var title = titleService.findLongestContainedBy(element.getWindowTitle());
        if (title == null) {
            element.setCounted(0);
            return element;
        }
        boolean isPositive = title.getType().equals(Title.Type.POSITIVE);
        if (isPositive && !conditionChecker.areConditionsMet(element.getGroupId())) {
            element.setCounted(0);
            return element;
        }
        element.setCounted(isPositive ? Math.abs(elapsed) : - Math.abs(elapsed));
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
