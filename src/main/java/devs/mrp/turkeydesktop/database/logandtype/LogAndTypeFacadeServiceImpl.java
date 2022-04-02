/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.closeables.CloseableServiceFactory;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogRepository;
import devs.mrp.turkeydesktop.database.titles.TitleServiceFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeRepository;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeService;

/**
 *
 * @author miguel
 */
public class LogAndTypeFacadeServiceImpl implements LogAndTypeFacadeService {

    private final LogAndTypeFacadeDao repo = LogAndTypeFacadeRepository.getInstance();
    private final TimeLogRepository logRepo = TimeLogRepository.getInstance();
    private final TypeRepository typeRepo = TypeRepository.getInstance();
    private final TimeLogService logService = TimeLogServiceFactory.getService();
    private final TypeService typeService = TypeServiceFactory.getService();
    private final IConfigElementService configService = FConfigElementService.getService();
    private final TitleService titleService = TitleServiceFactory.getService();
    private final IGroupAssignationService groupAssignationService = FGroupAssignationService.getService();
    private final IConditionService conditionService = FConditionService.getService();
    private final CloseableService closeableService = CloseableServiceFactory.getService();
    
    private final ConditionChecker conditionChecker = ConditionCheckerFactory.getConditionChecker();
    
    private static final Logger LOGGER = Logger.getLogger(LogAndTypeFacadeServiceImpl.class.getName());

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
        boolean lockdown = conditionChecker.isLockDownTime();
        boolean idle = conditionChecker.isIdle();
        int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
        if (type == null || type.getType() == null) {
            type = new Type();
            type.setType(Type.Types.UNDEFINED);
        }
        element.setBlockable(false);
        switch (type.getType()){ // TODO make chain of responsibility to handle each case in a more clean way
            case NEUTRAL:
                element.setType(Type.Types.NEUTRAL);
                element.setGroupId(-1);
                element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                break;
            case UNDEFINED:
                element.setType(Type.Types.UNDEFINED);
                element.setGroupId(-1);
                element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                break;
            case DEPENDS:
                element.setType(Type.Types.DEPENDS);
                // If title is "hello to you" and we have records "hello" in group1 and "hello to" in group2 the group2 will be chosen
                GroupAssignation assignation = groupAssignationService.findLongestTitleIdContainedIn(element.getWindowTitle());
                element.setGroupId(assignation != null ? assignation.getGroupId() : -1);
                if (!lockdown){setCountedDependingOnTitle(element, element.getElapsed());} else {setCountedForTitleWhenLockdown(element, proportion);}
                break;
            case POSITIVE:
                element.setType(Type.Types.POSITIVE);
                GroupAssignation positiveAssignation = groupAssignationService.findByProcessId(element.getProcessName());
                element.setGroupId(positiveAssignation != null ? positiveAssignation.getGroupId() : -1);
                if (!lockdown) {
                    element.setCounted(!conditionChecker.isIdleWithToast() && conditionChecker.areConditionsMet(element.getGroupId()) ? Math.abs(element.getElapsed()) : 0);
                } else if (!conditionChecker.isIdle()) { // when in lockdown, don't disccount points if idle
                    element.setCounted(-1 * proportion * element.getElapsed());
                }
                break;
            case NEGATIVE:
                element.setType(Type.Types.NEGATIVE);
                GroupAssignation negativeAssignation = groupAssignationService.findByProcessId(element.getProcessName());
                element.setGroupId(negativeAssignation != null ? negativeAssignation.getGroupId() : -1);
                element.setCounted(Math.abs(element.getElapsed()) * proportion * (-1));
                element.setBlockable(true);
                break;
            default:
                break;
        }
        
        return element;
    }
    
    private TimeLog setCountedForTitleWhenLockdown(TimeLog element, long proportion) {
        var title = titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase());
        if (title != null && title.getType().equals(Title.Type.NEGATIVE)) {
            element.setBlockable(closeableService.canBeClosed(element.getProcessName()));
            element.setCounted(-1 * proportion * element.getElapsed());
        } else if (!conditionChecker.isIdle()) { // when not negative, don't disccount points if idle
            element.setCounted(-1 * proportion * element.getElapsed());
        } else {
            element.setCounted(0);
        }
        return element;
    }
    
    private TimeLog setCountedDependingOnTitle(TimeLog element, long elapsed) {
        var title = titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase());
        if (title == null) {
            element.setCounted(0);
            return element;
        }
        boolean isPositive = title.getType().equals(Title.Type.POSITIVE);
        if (isPositive && (conditionChecker.isIdleWithToast() || !conditionChecker.areConditionsMet(element.getGroupId()))) {
            element.setCounted(0);
            return element;
        }
        element.setCounted(isPositive ? Math.abs(elapsed) : - Math.abs(elapsed));
        element.setBlockable(closeableService.canBeClosed(element.getProcessName()));
        return element;
    }
    
    private TimeLog adjustAccumulated(TimeLog element, long counted) {
        TimeLog last = logService.findMostRecent();
        long lastAccumulated = 0;
        if (last != null) {
            lastAccumulated = last.getAccumulated();
        }
        long accumulated;
        accumulated = lastAccumulated + counted;
        element.setAccumulated(accumulated);
        //LOGGER.log(Level.INFO, "accumulated: " + accumulated);
        return element;
    }

}
