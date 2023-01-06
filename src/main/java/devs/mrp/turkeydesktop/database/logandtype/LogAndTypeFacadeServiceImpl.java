/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.closeables.CloseableFactory;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.type.TypeServiceFactory;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.sql.SQLException;
import java.util.Date;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.apache.commons.lang3.StringUtils;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;

/**
 *
 * @author miguel
 */
public class LogAndTypeFacadeServiceImpl implements LogAndTypeFacadeService {

    private final LogAndTypeFacadeDao repo = LogAndTypeFacadeRepository.getInstance();
    private final TimeLogService logService = TimeLogServiceFactory.getService();
    private final TypeService typeService = TypeServiceFactory.getService();
    private final ConfigElementService configService = ConfigElementFactory.getService();
    private final TitleService titleService = TitleFactory.getService();
    private final GroupAssignationService groupAssignationService = GroupAssignationFactory.getService();
    private final CloseableService closeableService = CloseableFactory.getService();

    private final ConditionChecker conditionChecker = ConditionCheckerFactory.getConditionChecker();

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

    private Single<TimeLog> flatMapFrom(ConfigElement proportionResult, Type myType, TimeLog element, Boolean lockdown, boolean idle) {
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
                return element.setBlockable(false);
            case UNDEFINED:
                element.setType(Type.Types.UNDEFINED);
                element.setGroupId(-1);
                element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                return element.setBlockable(false);
            case DEPENDS:
                element.setType(Type.Types.DEPENDS);
                return titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase())
                        .switchIfEmpty(Single.just(new Title()))
                        .flatMap(title -> {
                            if (title.getSubStr() == null) {
                                title.setSubStr(StringUtils.EMPTY);
                            }
                            if (title.getType() == null) {
                                title.setType(Title.Type.NEUTRAL);
                            }
                            if (StringUtils.EMPTY.equals(title.getSubStr())){
                                element.setGroupId(-1);
                                if (!lockdown){
                                    return setCountedDependingOnTitle(element, title, element.getElapsed(), proportion);
                                }
                                return setCountedForTitleWhenLockdown(element, title, proportion);
                            }
                            return groupAssignationService.findGroupOfAssignation(title.getSubStr())
                                .defaultIfEmpty(GroupAssignation.builder().groupId(-1).build())
                                .flatMap(assignation -> {
                                    element.setGroupId(assignation.getGroupId());
                                    if (!lockdown){
                                        return setCountedDependingOnTitle(element, title, element.getElapsed(), proportion);
                                    }
                                    return setCountedForTitleWhenLockdown(element, title, proportion);
                                });
                        });
            case POSITIVE:
                element.setType(Type.Types.POSITIVE);
                return groupAssignationService.findByProcessId(element.getProcessName())
                        .defaultIfEmpty(GroupAssignation.builder().groupId(-1).build())
                        .flatMap(result -> {
                            element.setGroupId(result.getGroupId());
                            if (!lockdown) {
                                return Single.zip(conditionChecker.areConditionsMet(element.getGroupId()), conditionChecker.isIdleWithToast(true), (areMet, isIdle) -> {
                                    element.setCounted(!isIdle && areMet ? Math.abs(element.getElapsed()) : 0);
                                    return element.setBlockable(false).blockingGet();
                                });
                            } // when in lockdown, don't disccount points if idle
                            return conditionChecker.isIdle().flatMap(isIdle -> {
                                if (!isIdle) {
                                    element.setCounted(-1 * proportion * element.getElapsed());
                                    return element.setBlockable(false);
                                } else {
                                    element.setCounted(0);
                                    return element.setBlockable(false);
                                }
                            });
                        });
            case NEGATIVE:
                element.setType(Type.Types.NEGATIVE);
                return groupAssignationService.findByProcessId(element.getProcessName())
                        .defaultIfEmpty(GroupAssignation.builder().groupId(-1).build())
                        .flatMap(result -> {
                            element.setGroupId(result.getGroupId());
                            element.setCounted(Math.abs(element.getElapsed()) * proportion * (-1));
                            return element.setBlockable(true);
                        });
            default:
                return element.setBlockable(false);
        }
    }

    private Single<TimeLog> adjustDependingOnType(TimeLog element) {
        Single<Type> ty = typeService.findById(element.getProcessName()).defaultIfEmpty(Type.builder().process(StringUtils.EMPTY).type(Type.Types.UNDEFINED).build());
        Single<Boolean> lock = conditionChecker.isLockDownTime();
        Single<Boolean> idl = conditionChecker.isIdle();
        Single<ConfigElement> prop = configService.configElement(ConfigurationEnum.PROPORTION);
        return Single.zip(ty, lock, idl, prop, (myType, lockdown, idle, proportionResult) -> {
            return flatMapFrom(proportionResult, myType, element, lockdown, idle).blockingGet();
        });
    }

    private Single<TimeLog> setCountedForTitleWhenLockdown(TimeLog element, Title title, long proportion) {
        if (title != null && title.getType().equals(Title.Type.NEGATIVE)) {
            return closeableService.canBeClosed(element.getProcessName()).flatMap(b -> {
                element.setCounted(-1 * proportion * element.getElapsed());
                return element.setBlockable(b);
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
                return closeableService.canBeClosed(element.getProcessName()).flatMap(b -> element.setBlockable(b));
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
