/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.SingleConsumerFactory;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.group.GroupServiceFactory;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupServiceFactory;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.imports.ImportServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.IdleChainCommander;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.LongWrapper;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReader;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReaderFactory;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.group.GroupService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public class ConditionCheckerImpl implements ConditionChecker {

    private IConditionService conditionService = FConditionService.getService();
    private GroupService groupService = GroupServiceFactory.getService();
    private TimeLogService timeLogService = TimeLogServiceFactory.getService();
    private IConfigElementService configService = FConfigElementService.getService();
    private ImportService importService = ImportServiceFactory.getService();
    private ChainHandler<LongWrapper> idleHandler = new IdleChainCommander().getHandlerChain();
    private ExternalGroupService externalGroupService = ExternalGroupServiceFactory.getService();
    private ImportReader importReader = ImportReaderFactory.getReader();
    private static long avoidMessageFlood = 1000*90; // if the idle time surpases 2+ minutes stop flooding notifications
    
    private Logger logger = Logger.getLogger(ConditionCheckerImpl.class.getName());

    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+$");

    @Override
    public void isConditionMet(Condition condition, Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
        TimeConverter.beginningOfOffsetDaysConsideringDayChange(condition.getLastDaysCondition(), beginningResult -> {
            TimeConverter.endOfTodayConsideringDayChange(endResult -> {
                timeLogService.timeSpentOnGroupForFrame(condition.getTargetId(), beginningResult, endResult, timeSpent -> {
                    externalTimeFromCondition(condition, external -> {
                        consumer.accept(timeSpent+external >= condition.getUsageTimeCondition());
                    });
                });
            });
        });
        
    }
    
    private void externalTimeFromCondition(Condition condition, LongConsumer c) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(c);
        configService.configElement(ConfigurationEnum.CHANGE_OF_DAY, changeOfDayResult -> {
            Long changeOfDay = Long.valueOf(changeOfDayResult.getValue());
            LocalDate to = LocalDateTime.now().minusHours(changeOfDay).toLocalDate();
            LocalDate from = LocalDateTime.now().minusHours(changeOfDay).minusDays(condition.getLastDaysCondition()).toLocalDate();
            externalGroupService.findByGroup(condition.getTargetId(), externals -> {
                var res = externals.stream()
                        .map(ExternalGroup::getFile)
                        .map(file -> importReader.getTotalSpentFromFileBetweenDates(file, from, to))
                        .collect(Collectors.summingLong(l -> l));
                consumer.accept(res);
            });
        });
    }

    @Override
    public void areConditionsMet(List<Condition> conditions, Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
        Set<Condition> processed = Collections.synchronizedSet(new HashSet<>());
        AtomicBoolean areMet = new AtomicBoolean(true);
        if (conditions.isEmpty()) {
            consumer.accept(true);
        }
        conditions.forEach(condition -> {
            isConditionMet(condition, isMet -> {
                if (!isMet) {
                    areMet.set(false);
                }
                processed.add(condition);
                if(processed.size() == conditions.size()) {
                    consumer.accept(areMet.get());
                }
            });
        });
    }

    @Override
    public void areConditionsMet(long groupId, Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
        if (groupId <= 0) {
            consumer.accept(true);
            return;
        }
        conditionService.findByGroupId(groupId, conditions -> {
            areConditionsMet(conditions, consumer);
        });
    }

    @Override
    public void isLockDownTime(Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
        isLockDownTime(System.currentTimeMillis(), isLockDown -> {
            consumer.accept(isLockDown);
        });
    }

    @Override
    public void isLockDownTime(Long now, Consumer<Boolean> c) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(c);
        lockDownActivated(lockDownActivatedResult -> {
            if (!lockDownActivatedResult){
                consumer.accept(false);
                return;
            }
            Long hourNow = TimeConverter.epochToMilisOnGivenDay(now);
            lockDownStart(lockDownStartResult -> {
                lockDownEnd(lockDownEndResult -> {
                    Long from = lockDownStartResult;
                    Long to = lockDownEndResult;
                    // if start equals end then return
                    if (from.equals(to)){
                        consumer.accept(false);
                        return;
                    }
                    boolean isLockDown = (from < to && (from <= hourNow && hourNow <= to))
                            || (from > to && (hourNow >= from || hourNow <= to));
                    if (isLockDown) {
                        Toaster.sendToast(localeMessages.getString("isLockDown"));
                    } else {
                        closeToLock(hourNow, from, res -> {
                            if (res)
                            Toaster.sendToast(localeMessages.getString("closeToLock"));
                        });
                    }
                    consumer.accept(isLockDown);
                });
            });
        });
    }
    
    private void lockDownActivated(Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        configService.findById(ConfigurationEnum.LOCKDOWN, c -> {
            consumer.accept(Boolean.valueOf(c.getValue()));
        });
    }

    private void lockDownStart(LongConsumer sc) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(sc);
        configService.findById(ConfigurationEnum.LOCKDOWN_FROM, c -> {
            consumer.accept(Long.valueOf(c.getValue()));
        });
    }

    private void lockDownEnd(LongConsumer sc) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(sc);
        configService.findById(ConfigurationEnum.LOCKDOWN_TO, c -> {
            consumer.accept(Long.valueOf(c.getValue()));
        });
    }

    private void closeToLock(Long hourNow, Long from, Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        lockDownActivated(lockDownActivated -> {
            if (!lockDownActivated) {
                consumer.accept(false);
                return;
            }
            configService.findById(ConfigurationEnum.LOCK_NOTIFY, lockNotify -> {
                if (!Boolean.valueOf(lockNotify.getValue())) {
                    consumer.accept(false);
                    return;
                }
                lockDownStart(lockDownStart -> {
                    lockDownEnd(lockDownEnd -> {
                        if (lockDownStart == lockDownEnd) {
                            consumer.accept(false);
                            return;
                        }
                        configService.findById(ConfigurationEnum.LOCK_NOTIFY_MINUTES, lockNotifyMinutes -> {
                            Long minutesNotice = TimeConverter.getMinutes(Long.valueOf(lockNotifyMinutes.getValue()));
                            if (hourNow < from) {
                                consumer.accept(from - hourNow < 60 * 1000 * minutesNotice);
                                return;
                            } else if (hourNow > from) {
                                consumer.accept(from + TimeConverter.hoursToMilis(24) - hourNow < 60 * 1000 * minutesNotice);
                                return;
                            }
                            consumer.accept(false);
                        });
                    });
                });
            });
        });
    }

    @Override
    public void isTimeRunningOut(Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        configService.findById(ConfigurationEnum.MIN_LEFT_BUTTON, minLeftButton -> {
            Boolean notify = Boolean.valueOf(minLeftButton.getValue());
            if (!notify) {
                consumer.accept(false);
                return;
            }
            configService.findById(ConfigurationEnum.MIN_LEFT_QTY, minLeftQty -> {
                Long notification = Long.valueOf(minLeftQty.getValue());
                timeRemaining(timeRemaining -> {
                    consumer.accept(notification >= timeRemaining);
                });
            });
        });
    }

    @Override
    public void timeRemaining(LongConsumer sc) {
        LongConsumer consumer = SingleConsumerFactory.getLongConsumer(sc);
        importService.findAll(allResult -> {
            Long totalImported = allResult.stream()
                    .map(path -> {
                        String firstLine = "";
                        try {
                            firstLine = FileHandler.readFirstLineFromFile(new File(path));
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, "Cannot read from file " + path, e);
                        }
                        return firstLine;
                    })
                    .filter(Objects::nonNull) // filter nulls
                    .filter(s -> !s.isBlank()) // filter blanks
                    .filter(s -> NUMBER_PATTERN.matcher(s).matches()) // filter non numbers positive or negative
                    .collect(Collectors.summingLong(Long::valueOf)); // convert to long and sum up
            timeLogService.findMostRecent(tl -> {
                Long accumulated = tl != null ? tl.getAccumulated() : 0;
                configService.findById(ConfigurationEnum.PROPORTION, foundProportion -> {
                    Long proportion = Long.valueOf(foundProportion.getValue());
                    consumer.accept((accumulated + totalImported)/proportion);
                });
            });
        });
    }
    
    @Override
    public void isIdle(Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        configService.findById(ConfigurationEnum.IDLE, idle -> {
            Long idleCondition = Long.valueOf(idle.getValue());
            LongWrapper currentIdle = new LongWrapper();
            idleHandler.receiveRequest("idle", currentIdle);
            consumer.accept(currentIdle.getValue() >= idleCondition);
        });
        
    }
    
    private void isIdleFlood(Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        configService.findById(ConfigurationEnum.IDLE, idle -> {
            Long idleCondition = Long.valueOf(idle.getValue());
            LongWrapper currentIdle = new LongWrapper();
            idleHandler.receiveRequest("idle", currentIdle);
            consumer.accept(currentIdle.getValue() >= idleCondition + avoidMessageFlood);
        });
    }
    
    @Override
    public void isIdleWithToast(boolean sendToast, Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        isIdle(idle -> {
            isIdleFlood(flood -> {
                if (idle && sendToast && !flood) {
                    Toaster.sendToast(localeMessages.getString("idleMsg"));
                }
            });
            consumer.accept(idle);
        });
    }

    @Override
    public void notifyCloseToConditionsRefresh() {
        closeToConditionsRefresh(result -> {
            if (result) {
                Toaster.sendToast(localeMessages.getString("conditionsRefreshSoon"));
            }
        });
    }
    
    public void closeToConditionsRefresh(Consumer<Boolean> sc) {
        Consumer<Boolean> consumer = SingleConsumerFactory.getBooleanConsumer(sc);
        configService.configElement(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY, notifyChangeOfDayResult -> {
            Boolean notify = Boolean.valueOf(notifyChangeOfDayResult.getValue());
            if (!notify) {
                consumer.accept(false);
                return;
            }
            configService.findById(ConfigurationEnum.CHANGE_OF_DAY, changeOfDayResult -> {
                configService.findById(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY_MINUTES, changeOfDayMinutes -> {
                    Long changeOfDay = TimeConverter.hoursToMilis(Long.valueOf(changeOfDayResult.getValue()));

                    Long minutesNotice = Long.valueOf(changeOfDayMinutes.getValue());
                    Long hourNow = TimeConverter.epochToMilisOnGivenDay(System.currentTimeMillis());
                    if (hourNow < changeOfDay) {
                        consumer.accept(changeOfDay - hourNow < 60 * 1000 * minutesNotice);
                        return;
                    } else if (hourNow > changeOfDay) {
                        consumer.accept(changeOfDay + TimeConverter.hoursToMilis(24) - hourNow < 60 * 1000 * minutesNotice);
                        return;
                    }
                    consumer.accept(false);
                });
            });
        });
    }

}
