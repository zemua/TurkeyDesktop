package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.closeables.CloseableService;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.sql.SQLException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class LogAndTypeFacadeServiceImpl implements LogAndTypeFacadeService {
    
    private final LogAndTypeFacadeFactory factory;
    private final LogAndTypeFacadeDao repo;
    private final TimeLogService logService;
    private final TypeService typeService;
    private final TitleService titleService;
    private final GroupAssignationService groupAssignationService;
    private final CloseableService closeableService;
    private final ConditionChecker conditionChecker;
    private final GroupService groupService;
    private final ConfigElementService configService;
    private final TimeConverter timeConverter;
    private final Toaster toaster;
    
    private final LocaleMessages localeMessages = LocaleMessages.getInstance();
    
    public LogAndTypeFacadeServiceImpl(LogAndTypeFacadeFactory factory) {
        this.conditionChecker = factory.conditionChecker();
        this.configService = factory.configService();
        this.repo = factory.getRepo();
        this.groupAssignationService = factory.getGroupAssignationService();
        this.closeableService = factory.getCloseableService();
        this.timeConverter = factory.getTimeConverter();
        this.logService = factory.getTimeLogService();
        this.typeService = factory.getTypeService();
        this.titleService = factory.getTitleService();
        this.groupService = factory.getGroupService();
        this.toaster = factory.getToaster();
        this.factory = factory;
    }

    @Override
    public Observable<Tripla<String, Long, Type.Types>> getTypedLogGroupedByProcess(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = timeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = timeConverter.millisToEndOfDay(to.getTime());
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

    private Single<TimeLog> flatMapFrom(ConfigElement proportionResult, Type myType, TimeLog elementParam, Boolean lockdown, boolean idle) {
        int proportion = Integer.valueOf(proportionResult.getValue());
        Type type = myType;
        if (type == null || type.getType() == null) {
            type = new Type();
            type.setType(Type.Types.UNDEFINED);
        }
        TimeLog element = factory.asNotBlockable(elementParam).blockingGet();
        switch (type.getType()){ // TODO use strategy to handle each case in a more clean way
            case NEUTRAL:
                element.setType(Type.Types.NEUTRAL);
                element.setGroupId(-1);
                element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                return factory.asNotBlockable(element);
            case UNDEFINED:
                element.setType(Type.Types.UNDEFINED);
                element.setGroupId(-1);
                element.setCounted(lockdown && !idle ? -1 * proportion * element.getElapsed() : 0);
                if (!idle) {
                    toaster.sendToast(localeMessages.getString("notCategorized"));
                }
                return factory.asNotBlockable(element);
            case DEPENDS:
                element.setType(Type.Types.DEPENDS);
                return titleService.findLongestContainedBy(element.getWindowTitle().toLowerCase())
                        .switchIfEmpty(Single.just(new Title()))
                        .flatMap(title -> {
                            log.debug("Type {} for title in this window is: {}", title.getType(), title.getSubStr());
                            if (title.getSubStr() == null) {
                                title.setSubStr(StringUtils.EMPTY);
                            }
                            if (title.getType() == null) {
                                if (!idle) {
                                    toaster.sendToast(localeMessages.getString("notCategorized"));
                                }
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
                                    log.debug("This is assigned to group: {}", assignation);
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
                                // TODO check if summing is enabled for this group
                                return Single.zip(conditionChecker.areConditionsMet(element.getGroupId()),
                                        conditionChecker.isIdleWithToast(true),
                                        groupService.isDisablePoints(element.getGroupId()),
                                        (conditionsMet, isIdle, disabledPoints) -> {
                                    element.setIdle(isIdle);
                                    log.debug("Setting iddle to {}", isIdle);
                                    element.setCounted(!isIdle && conditionsMet && !disabledPoints ? Math.abs(element.getElapsed()) : 0);
                                    return factory.asNotBlockable(element).blockingGet();
                                });
                            } // when in lockdown, don't disccount points if idle
                            return conditionChecker.isIdle().flatMap(isIdle -> {
                                element.setIdle(isIdle);
                                log.debug("Setting iddle to {}", isIdle);
                                if (!isIdle) {
                                    element.setCounted(-1 * proportion * element.getElapsed());
                                    return factory.asNotBlockable(element);
                                } else {
                                    element.setCounted(0);
                                    return factory.asNotBlockable(element);
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
                            return factory.asBlockable(element);
                        });
            default:
                return factory.asNotBlockable(element);
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
                Single<TimeLog> result;
                if (b) {
                    result = factory.asBlockable(element);
                } else {
                    result = factory.asNotBlockable(element);
                }
                return result;
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
                element.setIdle(isIdle);
                log.debug("Setting iddle to {}", isIdle);
                if (isNeutral || (isPositive && (isIdle || !areMet))) {
                    element.setCounted(0);
                    return Single.just(element);
                }
                return groupService.isDisablePoints(element.getGroupId())
                        .flatMap(disabledSum -> {
                            if (isPositive && disabledSum) {
                                log.debug("Sum disabled in positive, setting counted to 0");
                                element.setCounted(0);
                            } else {
                                log.debug("Adjusting counted for positive/negative");
                                element.setCounted(isPositive ? Math.abs(elapsed) : - Math.abs(elapsed) * proportion);
                            }
                            
                            return closeableService.canBeClosed(element.getProcessName()).flatMap(b -> {
                            Single<TimeLog> result;
                            if (b) {
                                result = factory.asBlockable(element);
                            } else {
                                result = factory.asNotBlockable(element);
                            }
                            return result;
                            });
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
