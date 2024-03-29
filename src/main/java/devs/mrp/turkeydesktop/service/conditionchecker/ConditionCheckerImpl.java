package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.LongWrapper;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReader;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConditionCheckerImpl implements ConditionChecker {

    private final ConditionService conditionService;
    private final TimeLogService timeLogService;
    private final ConfigElementService configService;
    private final ImportService importService;
    private final ChainHandler<LongWrapper> idleHandler;
    private final ExternalGroupService externalGroupService;
    private final ImportReader importReader;
    private static long avoidMessageFlood = 1000*90; // if the idle time surpases 2+ minutes stop flooding notifications
    private final TimeConverter timeConverter;
    private final FileHandler fileHandler;
    
    private Logger logger = Logger.getLogger(ConditionCheckerImpl.class.getName());

    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+$");
    private final Toaster toaster;
    
    public ConditionCheckerImpl(ConditionCheckerFactory factory) {
        this.configService = factory.getConfigElementService();
        this.toaster = factory.getToaster();
        this.timeConverter = factory.getTimeConverter();
        this.fileHandler = factory.fileHandler();
        this.conditionService = factory.getConditionService();
        this.timeLogService = factory.getTimeLogService();
        this.idleHandler = factory.gettIdleHandler();
        this.importReader = factory.getImportReader();
        this.importService = factory.getImportService();
        this.externalGroupService = factory.getExternalGroupService();
    }

    @Override
    public Single<Boolean> isConditionMet(Condition condition) {
        SingleSource<Long> beginning = timeConverter.beginningOfOffsetDaysConsideringDayChange(condition.getLastDaysCondition());
        SingleSource<Long> end = timeConverter.endOfTodayConsideringDayChange();
        return Single.zip(beginning, end, (beginningResult, endResult) -> {
            log.debug("Getting time spent on group {} from epoch {} to {}", condition.getTargetId(), beginningResult, endResult);
            Single<Long> spent = timeLogService.timeSpentOnGroupForFrame(condition.getTargetId(), beginningResult, endResult);
            Single<Long> ext = externalTimeFromCondition(condition);
            return Single.zip(spent, ext, (timeSpent, external) -> {
                return timeSpent+external >= condition.getUsageTimeCondition();
            }).blockingGet();
        });
    }
    
    private Single<Long> externalTimeFromCondition(Condition condition) {
        return configService.configElement(ConfigurationEnum.CHANGE_OF_DAY).flatMap(changeOfDayResult -> {
            Long changeOfDay = Long.valueOf(changeOfDayResult.getValue());
            LocalDate to = LocalDateTime.now().minusHours(changeOfDay).toLocalDate();
            LocalDate from = LocalDateTime.now().minusHours(changeOfDay).minusDays(condition.getLastDaysCondition()).toLocalDate();
            return externalGroupService.findByGroup(condition.getTargetId())
                    .map(ExternalGroup::getFile)
                    .flatMapSingle(file -> importReader.getTotalSpentFromFileBetweenDates(file, from, to))
                    .collect(AtomicLong::new, AtomicLong::addAndGet)
                    .map(AtomicLong::longValue);
            });
    }

    @Override
    public Single<Boolean> areConditionsMet(List<Condition> conditions) {
        if (conditions.isEmpty()) {
            return Single.just(true);
        }
        return Observable.fromIterable(conditions)
                .doOnNext(cond -> log.debug("Checking condition for target groupId {}",cond.toString()))
                .flatMapSingle(this::isConditionMet)
                .doOnNext(isMet -> log.debug("Condition met: {}", isMet))
                .filter(b -> Boolean.FALSE.equals(b))
                .first(Boolean.TRUE);
    }

    @Override
    public Single<Boolean> areConditionsMet(long groupId) {
        if (groupId <= 0) {
            return Single.just(true);
        }
        return conditionService.findByGroupId(groupId)
                .toList()
                .flatMap(conditions -> areConditionsMet(conditions));
    }

    @Override
    public Single<Boolean> isLockDownTime() {
        return isLockDownTime(System.currentTimeMillis());
    }

    @Override
    public Single<Boolean> isLockDownTime(Long now) {
        return lockDownActivated().flatMap(lockDownActivatedResult -> {
            if (!lockDownActivatedResult){
                return Single.just(false);
            }
            Long hourNow = timeConverter.epochToMilisOnGivenDay(now);
            return lockDownStart().flatMap(lockDownStartResult -> {
                return lockDownEnd().flatMap(lockDownEndResult -> {
                    Long from = lockDownStartResult;
                    Long to = lockDownEndResult;
                    // if start equals end then return
                    if (from.equals(to)){
                        return Single.just(false);
                    }
                    boolean isLockDown = (from < to && (from <= hourNow && hourNow <= to))
                            || (from > to && (hourNow >= from || hourNow <= to));
                    if (isLockDown) {
                        toaster.sendToast(localeMessages.getString("isLockDown"));
                    } else {
                        return closeToLock(hourNow, from).map(res -> {
                            if (res) {
                                toaster.sendToast(localeMessages.getString("closeToLock"));
                            }
                            return false; // close but not yet
                        });
                    }
                    return Single.just(isLockDown);
                });
            });
        });
    }
    
    private Single<Boolean> lockDownActivated() {
        return configService.findById(ConfigurationEnum.LOCKDOWN)
                .map(ConfigElement::getValue)
                .map(Boolean::valueOf);
    }

    private Single<Long> lockDownStart() {
        return configService.findById(ConfigurationEnum.LOCKDOWN_FROM)
                .map(ConfigElement::getValue)
                .map(Long::valueOf);
    }

    private Single<Long> lockDownEnd() {
        return configService.findById(ConfigurationEnum.LOCKDOWN_TO)
                .map(ConfigElement::getValue)
                .map(Long::valueOf);
    }

    private Single<Boolean> closeToLock(Long hourNow, Long from) {
        return lockDownActivated().flatMap(lockDownActivated -> {
            if (!lockDownActivated) {
                return Single.just(false);
            }
            return configService.findById(ConfigurationEnum.LOCK_NOTIFY).flatMap(lockNotify -> {
                if (!Boolean.valueOf(lockNotify.getValue())) {
                    return Single.just(false);
                }
                return lockDownStart().flatMap(lockDownStart -> {
                    return lockDownEnd().flatMap(lockDownEnd -> {
                        if (lockDownStart.equals(lockDownEnd)) {
                            return Single.just(false);
                        }
                        return configService.findById(ConfigurationEnum.LOCK_NOTIFY_MINUTES).map(lockNotifyMinutes -> {
                            Long minutesNotice = timeConverter.getMinutes(Long.valueOf(lockNotifyMinutes.getValue()));
                            if (hourNow < from) {
                                return (from - hourNow < 60 * 1000 * minutesNotice);
                            } else if (hourNow > from) {
                                return (from + timeConverter.hoursToMilis(24) - hourNow < 60 * 1000 * minutesNotice);
                            }
                            return false;
                        });
                    });
                });
            });
        });
    }

    @Override
    public Single<Boolean> isTimeRunningOut() {
        return configService.findById(ConfigurationEnum.MIN_LEFT_BUTTON).flatMap(minLeftButton -> {
            Boolean notify = Boolean.valueOf(minLeftButton.getValue());
            if (!notify) {
                return Single.just(false);
            }
            return configService.findById(ConfigurationEnum.MIN_LEFT_QTY).flatMap(minLeftQty -> {
                Long notification = Long.valueOf(minLeftQty.getValue());
                return timeRemaining().map(timeRemaining -> {
                    return notification >= timeRemaining;
                });
            });
        });
    }

    @Override
    public Single<Long> timeRemaining() {
        return importService.findAll().map(path -> {
                        String firstLine = "";
                        try {
                            firstLine = fileHandler.readFirstLineFromFile(new File(path));
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, "Cannot read from file " + path, e);
                        }
                        return firstLine;
                    })
                .filter(Objects::nonNull) // filter nulls
                .filter(s -> !s.isBlank()) // filter blanks
                .filter(s -> NUMBER_PATTERN.matcher(s).matches()) // filter non numbers positive or negative
                .map(Long::valueOf)
                .collect(AtomicLong::new, AtomicLong::addAndGet)
                .map(AtomicLong::longValue)
                .flatMap(totalImported -> {
                        return timeLogService.findMostRecent().flatMap(tl -> {
                            Long accumulated = tl != null ? tl.getAccumulated() : 0;
                            return configService.findById(ConfigurationEnum.PROPORTION).map(foundProportion -> {
                                Long proportion = Long.valueOf(foundProportion.getValue());
                                return (accumulated + totalImported)/proportion;
                            });
                        });
                });
    }
    
    @Override
    public Single<Boolean> isIdle() {
        return configService.findById(ConfigurationEnum.IDLE).map(idle -> {
            Long idleCondition = Long.valueOf(idle.getValue());
            LongWrapper currentIdle = new LongWrapper();
            idleHandler.receiveRequest("idle", currentIdle);
            return currentIdle.getValue() >= idleCondition;
        });
        
    }
    
    private Single<Boolean> isIdleFlood() {
        return configService.findById(ConfigurationEnum.IDLE).map(idle -> {
            Long idleCondition = Long.valueOf(idle.getValue());
            LongWrapper currentIdle = new LongWrapper();
            idleHandler.receiveRequest("idle", currentIdle);
            return currentIdle.getValue() >= idleCondition + avoidMessageFlood;
        });
    }
    
    @Override
    public Single<Boolean> isIdleWithToast(boolean sendToast) {
        return isIdle().flatMap(idle -> {
            return isIdleFlood().map(flood -> {
                if (idle && sendToast && !flood) {
                    toaster.sendToast(localeMessages.getString("idleMsg"));
                }
                return idle;
            });
        });
    }

    @Override
    public Single<Boolean> notifyCloseToConditionsRefresh() {
        return closeToConditionsRefresh().map(result -> {
            if (result) {
                toaster.sendToast(localeMessages.getString("conditionsRefreshSoon"));
            }
            return result;
        });
    }
    
    public Single<Boolean> closeToConditionsRefresh() {
        return configService.configElement(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY).flatMap(notifyChangeOfDayResult -> {
            Boolean notify = Boolean.valueOf(notifyChangeOfDayResult.getValue());
            if (!notify) {
                return Single.just(false);
            }
            return configService.findById(ConfigurationEnum.CHANGE_OF_DAY).flatMap(changeOfDayResult -> {
                return configService.findById(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY_MINUTES).map(changeOfDayMinutes -> {
                    Long changeOfDay = timeConverter.hoursToMilis(Long.valueOf(changeOfDayResult.getValue()));

                    Long minutesNotice = Long.valueOf(changeOfDayMinutes.getValue());
                    Long hourNow = timeConverter.epochToMilisOnGivenDay(System.currentTimeMillis());
                    if (hourNow < changeOfDay) {
                        return changeOfDay - hourNow < 60 * 1000 * minutesNotice;
                    } else if (hourNow > changeOfDay) {
                        return changeOfDay + timeConverter.hoursToMilis(24) - hourNow < 60 * 1000 * minutesNotice;
                    }
                    return false;
                });
            });
        });
    }

}
