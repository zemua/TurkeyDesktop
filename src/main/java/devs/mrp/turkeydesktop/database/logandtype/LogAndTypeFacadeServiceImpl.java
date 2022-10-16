/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.closeables.CloseableServiceFactory;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
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
import java.util.function.Consumer;

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
    public void getTypedLogGroupedByProcess(Date from, Date to, Consumer<List<Tripla<String, Long, Type.Types>>> consumer) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        List<Tripla<String, Long, Type.Types>> typedTimes = new ArrayList<>();
        TurkeyAppFactory.runResultSetWorker(() -> repo.getTypedLogGroupedByProcess(fromMilis, toMilis), set -> {
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
            consumer.accept(typedTimes);
        });
    }

    @Override
    public void addTimeLogAdjustingCounted(TimeLog element, Consumer<TimeLog> consumer) {
        adjustDependingOnType(element, result -> {
            adjustAccumulated(element, element.getCounted(), updatedElement -> {
                logService.add(updatedElement,r -> {});
                consumer.accept(updatedElement);
            });
        });
    }
    
    private void adjustDependingOnType(TimeLog element, Consumer<TimeLog> consumer) {
        Type myType = typeService.findById(element.getProcessName());
        conditionChecker.isLockDownTime(lockdown -> {
            conditionChecker.isIdle(idle -> {
                configService.configElement(ConfigurationEnum.PROPORTION, proportionResult -> {
                    int proportion = Integer.valueOf(proportionResult.getValue());
                    Type type = myType;
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
                            consumer.accept(element);
                            break;
                        case UNDEFINED:
                            element.setType(Type.Types.UNDEFINED);
                            element.setGroupId(-1);
                            element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                            consumer.accept(element);
                            break;
                        case DEPENDS:
                            element.setType(Type.Types.DEPENDS);
                            // If title is "hello to you" and we have records "hello" in group1 and "hello to" in group2 the group2 will be chosen
                            groupAssignationService.findLongestTitleIdContainedIn(element.getWindowTitle(), assignation -> {
                                element.setGroupId(assignation != null ? assignation.getGroupId() : -1);
                                if (!lockdown){
                                    setCountedDependingOnTitle(element, element.getElapsed(), proportion, r -> consumer.accept(element));
                                } else {
                                    setCountedForTitleWhenLockdown(element, proportion, r -> consumer.accept(element));
                                }
                            });
                            break;
                        case POSITIVE:
                            element.setType(Type.Types.POSITIVE);
                            groupAssignationService.findByProcessId(element.getProcessName(), result -> {
                                element.setGroupId(result != null ? result.getGroupId() : -1);
                                if (!lockdown) {
                                    conditionChecker.areConditionsMet(element.getGroupId(), areMet -> {
                                        conditionChecker.isIdleWithToast(isIdle -> {
                                            element.setCounted(!isIdle && areMet ? Math.abs(element.getElapsed()) : 0);
                                            consumer.accept(element);
                                        });
                                    });
                                } else { // when in lockdown, don't disccount points if idle
                                    conditionChecker.isIdle(isIdle -> {
                                        if (!isIdle) {
                                            element.setCounted(-1 * proportion * element.getElapsed());
                                            consumer.accept(element);
                                        } else {
                                            element.setCounted(0);
                                            consumer.accept(element);
                                        }
                                    });
                                }
                            });
                            break;
                        case NEGATIVE:
                            element.setType(Type.Types.NEGATIVE);
                            groupAssignationService.findByProcessId(element.getProcessName(), result -> {
                                element.setGroupId(result != null ? result.getGroupId() : -1);
                                element.setCounted(Math.abs(element.getElapsed()) * proportion * (-1));
                                element.setBlockable(true);
                                consumer.accept(element);
                            });
                            break;
                        default:
                            consumer.accept(element);
                            break;
                    }
                });
            });
        });
    }
    
    private void setCountedForTitleWhenLockdown(TimeLog element, long proportion, Consumer<TimeLog> consumer) {
        var title = titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase());
        if (title != null && title.getType().equals(Title.Type.NEGATIVE)) {
            closeableService.canBeClosed(element.getProcessName(), b -> {
                element.setBlockable(b);
                element.setCounted(-1 * proportion * element.getElapsed());
                consumer.accept(element);
            });
        } else { // when not negative, don't disccount points if idle
            conditionChecker.isIdle(isIdle -> {
                if (!isIdle) {
                    element.setCounted(-1 * proportion * element.getElapsed());
                    consumer.accept(element);
                } else {
                    element.setCounted(0);
                    consumer.accept(element);
                }
            });
        }
    }
    
    private void setCountedDependingOnTitle(TimeLog element, long elapsed, int proportion, Consumer<TimeLog> consumer) {
        var title = titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase());
        if (title == null) {
            element.setCounted(0);
            consumer.accept(element);
            return;
        }
        boolean isPositive = title.getType().equals(Title.Type.POSITIVE);
        conditionChecker.areConditionsMet(element.getGroupId(), areMet -> {
            conditionChecker.isIdleWithToast(isIdle -> {
                if (isPositive && (isIdle || !areMet)) {
                    element.setCounted(0);
                    consumer.accept(element);
                } else {
                    element.setCounted(isPositive ? Math.abs(elapsed) : - Math.abs(elapsed) * proportion);
                    closeableService.canBeClosed(element.getProcessName(), b -> {
                        element.setBlockable(b);
                        consumer.accept(element);
                    });
                }
            });
            
        });
    }
    
    private void adjustAccumulated(TimeLog element, long counted, Consumer<TimeLog> consumer) {
        logService.findMostRecent(last -> {
            long lastAccumulated = 0;
            if (last != null) {
                lastAccumulated = last.getAccumulated();
            }
            long accumulated;
            accumulated = lastAccumulated + counted;
            // TODO add to accumulated imported value
            element.setAccumulated(accumulated);
            //LOGGER.log(Level.INFO, "accumulated: " + accumulated);
            consumer.accept(element);
        });
    }

}
