/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
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
import devs.mrp.turkeydesktop.database.logs.TimeLog;
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
    public boolean isConditionMet(Condition condition) {
        long timeSpent = timeLogService.timeSpentOnGroupForFrame(condition.getTargetId(),
                TimeConverter.beginningOfOffsetDaysConsideringDayChange(condition.getLastDaysCondition()),
                TimeConverter.endOfTodayConsideringDayChange());
        long external = externalTimeFromCondition(condition);
        return timeSpent+external >= condition.getUsageTimeCondition();
    }
    
    private long externalTimeFromCondition(Condition condition) {
        Long changeOfDay = Long.valueOf(configService.configElement(ConfigurationEnum.CHANGE_OF_DAY).getValue());
        LocalDate to = LocalDateTime.now().minusHours(changeOfDay).toLocalDate();
        LocalDate from = LocalDateTime.now().minusHours(changeOfDay).minusDays(condition.getLastDaysCondition()).toLocalDate();
        List<ExternalGroup> externals = externalGroupService.findByGroup(condition.getTargetId());
        return externals.stream()
                .map(ExternalGroup::getFile)
                .map(file -> importReader.getTotalSpentFromFileBetweenDates(file, from, to))
                .collect(Collectors.summingLong(l -> l));
    }

    @Override
    public boolean areConditionsMet(List<Condition> conditions) {
        return conditions.stream()
                .map(c -> isConditionMet(c))
                .allMatch(b -> b.equals(true));
    }

    @Override
    public boolean areConditionsMet(long groupId) {
        if (groupId <= 0) {
            return true;
        }
        List<Condition> conditions = conditionService.findByGroupId(groupId);
        return areConditionsMet(conditions);
    }

    @Override
    public boolean isLockDownTime() {
        return isLockDownTime(System.currentTimeMillis());
    }

    @Override
    public boolean isLockDownTime(Long now) {
        if (!lockDownActivated()){
            return false;
        }
        Long hourNow = TimeConverter.epochToMilisOnGivenDay(now);
        Long from = lockDownStart();
        Long to = lockDownEnd();
        // if start equals end then return
        if (from.equals(to)){
            return false;
        }
        boolean isLockDown = (from < to && (from <= hourNow && hourNow <= to))
                || (from > to && (hourNow >= from || hourNow <= to));
        if (isLockDown) {
            Toaster.sendToast(localeMessages.getString("isLockDown"));
        } else if (closeToLock(hourNow, from)) {
            Toaster.sendToast(localeMessages.getString("closeToLock"));
        }
        return isLockDown;
    }
    
    private boolean lockDownActivated() {
        return Boolean.valueOf(configService.findById(ConfigurationEnum.LOCKDOWN).getValue());
    }

    private Long lockDownStart() {
        return Long.valueOf(configService.findById(ConfigurationEnum.LOCKDOWN_FROM).getValue());
    }

    private Long lockDownEnd() {
        return Long.valueOf(configService.findById(ConfigurationEnum.LOCKDOWN_TO).getValue());
    }

    private boolean closeToLock(Long hourNow, Long from) {
        if (!lockDownActivated()) {
            return false;
        }
        // if no notice is activated
        if (!Boolean.valueOf(configService.findById(ConfigurationEnum.LOCK_NOTIFY).getValue())) {
            return false;
        }
        // if start equals end
        if (lockDownStart().equals(lockDownEnd())) {
            return false;
        }
        Long minutesNotice = TimeConverter.getMinutes(Long.valueOf(configService.findById(ConfigurationEnum.LOCK_NOTIFY_MINUTES).getValue()));
        if (hourNow < from) {
            return from - hourNow < 60 * 1000 * minutesNotice;
        } else if (hourNow > from) {
            return from + TimeConverter.hoursToMilis(24) - hourNow < 60 * 1000 * minutesNotice;
        }
        return false;
    }

    @Override
    public boolean isTimeRunningOut() {
        Boolean notify = Boolean.valueOf(configService.findById(ConfigurationEnum.MIN_LEFT_BUTTON).getValue());
        if (!notify) {
            return false;
        }
        Long notification = Long.valueOf(configService.findById(ConfigurationEnum.MIN_LEFT_QTY).getValue());
        return notification >= timeRemaining();
    }

    @Override
    public long timeRemaining() {
        Long totalImported = importService.findAll()
                .stream()
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
        TimeLog tl = timeLogService.findMostRecent();
        Long accumulated = tl != null ? tl.getAccumulated() : 0;
        Long proportion = Long.valueOf(configService.findById(ConfigurationEnum.PROPORTION).getValue());
        return (accumulated + totalImported)/proportion;
    }
    
    @Override
    public boolean isIdle() {
        Long idleCondition = Long.valueOf(configService.findById(ConfigurationEnum.IDLE).getValue());
        LongWrapper currentIdle = new LongWrapper();
        idleHandler.receiveRequest("idle", currentIdle);
        return currentIdle.getValue() >= idleCondition;
    }
    
    private boolean isIdleFlood() {
        Long idleCondition = Long.valueOf(configService.findById(ConfigurationEnum.IDLE).getValue());
        LongWrapper currentIdle = new LongWrapper();
        idleHandler.receiveRequest("idle", currentIdle);
        return currentIdle.getValue() >= idleCondition + avoidMessageFlood;
    }
    
    @Override
    public boolean isIdleWithToast() {
        boolean idle = isIdle();
        if (idle && !isIdleFlood()) {
            Toaster.sendToast(localeMessages.getString("idleMsg"));
        }
        return idle;
    }

    @Override
    public void notifyCloseToConditionsRefresh() {
        if (closeToConditionsRefresh()) {
            Toaster.sendToast(localeMessages.getString("conditionsRefreshSoon"));
        }
    }
    
    public boolean closeToConditionsRefresh() {
        Boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY).getValue());
        if (!notify) {
            return false;
        }
        Long changeOfDay = TimeConverter.hoursToMilis(Long.valueOf(configService.findById(ConfigurationEnum.CHANGE_OF_DAY).getValue()));

        Long minutesNotice = Long.valueOf(configService.findById(ConfigurationEnum.NOTIFY_CHANGE_OF_DAY_MINUTES).getValue());
        Long hourNow = TimeConverter.epochToMilisOnGivenDay(System.currentTimeMillis());
        if (hourNow < changeOfDay) {
            return changeOfDay - hourNow < 60 * 1000 * minutesNotice;
        } else if (hourNow > changeOfDay) {
            return changeOfDay + TimeConverter.hoursToMilis(24) - hourNow < 60 * 1000 * minutesNotice;
        }
        return false;
    }

}
