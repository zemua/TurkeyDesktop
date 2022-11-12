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
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.titles.TitleServiceFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeService;
import org.apache.commons.lang3.StringUtils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public class LogAndTypeFacadeServiceImpl implements LogAndTypeFacadeService {

    private final LogAndTypeFacadeDao repo = LogAndTypeFacadeRepository.getInstance();
    private final TimeLogService logService = TimeLogServiceFactory.getService();
    private final TypeService typeService = TypeServiceFactory.getService();
    private final IConfigElementService configService = FConfigElementService.getService();
    private final TitleService titleService = TitleServiceFactory.getService();
    private final IGroupAssignationService groupAssignationService = FGroupAssignationService.getService();
    private final CloseableService closeableService = CloseableServiceFactory.getService();
    
    private final ConditionChecker conditionChecker = ConditionCheckerFactory.getConditionChecker();
    
    private static final Logger LOGGER = Logger.getLogger(LogAndTypeFacadeServiceImpl.class.getName());

    @Override
    public Observable<Tripla<String, Long, Type.Types>> getTypedLogGroupedByProcess(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        return repo.getTypedLogGroupedByProcess(fromMilis, toMilis).flatMapObservable(set -> {
            return Observable.create(emitter -> {
                try {
                    while (set.next()) {
                        Tripla<String, Long, Type.Types> tripla = new Tripla<>();
                        tripla.setValue1(set.getString(TimeLog.PROCESS_NAME));
                        tripla.setValue2(set.getLong(3));
                        String type = set.getString(Type.TYPE);
                        tripla.setValue3(type != null ? Type.Types.valueOf(type) : Type.Types.UNDEFINED);
                        emitter.onNext(tripla);
                    }
                } catch (SQLException ex) {
                    emitter.onError(ex);
                }
                emitter.onComplete();
            });
        });
    }

    @Override
    public Single<TimeLog> addTimeLogAdjustingCounted(TimeLog element) {
        return adjustDependingOnType(element).flatMap(result -> {
            return adjustAccumulated(result, result.getCounted()).flatMap(updatedElement -> {
                return logService.add(updatedElement)
                        .map(r -> updatedElement);
            });
        });
    }
    
    private Single<TimeLog> adjustDependingOnType(TimeLog element) {
        return typeService.findById(element.getProcessName()).flatMap(myType -> {
            return conditionChecker.isLockDownTime().flatMap(lockdown -> {
                return conditionChecker.isIdle().flatMap(idle -> {
                    return configService.configElement(ConfigurationEnum.PROPORTION).flatMap(proportionResult -> {
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
                                return Single.just(element);
                            case UNDEFINED:
                                element.setType(Type.Types.UNDEFINED);
                                element.setGroupId(-1);
                                element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                                return Single.just(element);
                            case DEPENDS:
                                element.setType(Type.Types.DEPENDS);
                                // If title is "hello to you" and we have records "hello" in group1 and "hello to" in group2 the group2 will be chosen
                                return titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase()).flatMap(title -> {
                                    String subStr = title != null ? title.getSubStr() : StringUtils.EMPTY;
                                    return groupAssignationService.findGroupOfAssignation(subStr).flatMap(assignation -> {
                                        element.setGroupId(assignation != null ? assignation.getGroupId() : -1);
                                        if (!lockdown){
                                            return setCountedDependingOnTitle(element, title, element.getElapsed(), proportion);
                                        }
                                        return setCountedForTitleWhenLockdown(element, title, proportion);
                                    });
                                });
                            case POSITIVE:
                                element.setType(Type.Types.POSITIVE);
                                return groupAssignationService.findByProcessId(element.getProcessName()).flatMap(result -> {
                                    element.setGroupId(result != null ? result.getGroupId() : -1);
                                    if (!lockdown) {
                                        return conditionChecker.areConditionsMet(element.getGroupId()).flatMap(areMet -> {
                                            return conditionChecker.isIdleWithToast(true).map(isIdle -> {
                                                element.setCounted(!isIdle && areMet ? Math.abs(element.getElapsed()) : 0);
                                                return element;
                                            });
                                        });
                                    } // when in lockdown, don't disccount points if idle
                                    return conditionChecker.isIdle().map(isIdle -> {
                                        if (!isIdle) {
                                            element.setCounted(-1 * proportion * element.getElapsed());
                                            return element;
                                        } else {
                                            element.setCounted(0);
                                            return element;
                                        }
                                    });
                                });
                            case NEGATIVE:
                                element.setType(Type.Types.NEGATIVE);
                                return groupAssignationService.findByProcessId(element.getProcessName()).map(result -> {
                                    element.setGroupId(result != null ? result.getGroupId() : -1);
                                    element.setCounted(Math.abs(element.getElapsed()) * proportion * (-1));
                                    element.setBlockable(true);
                                    return element;
                                });
                            default:
                                return Single.just(element);
                        }
                    });
                });
            });
        });
    }
    
    private Single<TimeLog> setCountedForTitleWhenLockdown(TimeLog element, Title title, long proportion) {
        if (title != null && title.getType().equals(Title.Type.NEGATIVE)) {
            return closeableService.canBeClosed(element.getProcessName()).map(b -> {
                element.setBlockable(b);
                element.setCounted(-1 * proportion * element.getElapsed());
                return element;
            });
        } // when not negative, don't disccount points if idle
        return conditionChecker.isIdle().map(isIdle -> {
            if (!isIdle) {
                element.setCounted(-1 * proportion * element.getElapsed());
                return element;
            }
            element.setCounted(0);
            return element;
        });
    }
    
    private Single<TimeLog> setCountedDependingOnTitle(TimeLog element, Title title, long elapsed, int proportion) {
        if (title == null) {
            element.setCounted(0);
            return Single.just(element);
        }
        boolean isPositive = title.getType().equals(Title.Type.POSITIVE);
        boolean isNeutral = title.getType().equals(Title.Type.NEUTRAL);
        return conditionChecker.areConditionsMet(element.getGroupId()).flatMap(areMet -> {
            return conditionChecker.isIdleWithToast(isPositive).flatMap(isIdle -> {
                if (isNeutral || (isPositive && (isIdle || !areMet))) {
                    element.setCounted(0);
                    return Single.just(element);
                }
                element.setCounted(isPositive ? Math.abs(elapsed) : - Math.abs(elapsed) * proportion);
                return closeableService.canBeClosed(element.getProcessName()).map(b -> {
                    element.setBlockable(b);
                    return element;
                });
            });
        });
    }
    
    private Single<TimeLog> adjustAccumulated(TimeLog element, long counted) {
        return logService.findMostRecent().map(last -> {
            long lastAccumulated = 0;
            if (last != null) {
                lastAccumulated = last.getAccumulated();
            }
            long accumulated;
            accumulated = lastAccumulated + counted;
            // TODO add to accumulated imported value
            element.setAccumulated(accumulated);
            //LOGGER.log(Level.INFO, "accumulated: " + accumulated);
            return element;
        });
    }

}
