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
import devs.mrp.turkeydesktop.database.group.FGroupService;
import devs.mrp.turkeydesktop.database.group.IGroupService;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.imports.ImportServiceFactory;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.IdleChainCommander;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.LongWrapper;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.service.toaster.ToasterChainCommander;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author miguel
 */
public class ConditionChecker implements IConditionChecker {

    private IConditionService conditionService = FConditionService.getService();
    private IGroupService groupService = FGroupService.getService();
    private ITimeLogService timeLogService = FTimeLogService.getService();
    private IConfigElementService configService = FConfigElementService.getService();
    private ImportService importService = ImportServiceFactory.getService();
    private ChainHandler<LongWrapper> idleHandler = new IdleChainCommander().getHandlerChain();
    
    private Logger logger = Logger.getLogger(ConditionChecker.class.getName());

    private LocaleMessages localeMessages = LocaleMessages.getInstance();

    @Override
    public boolean isConditionMet(Condition condition) { // TODO check also imported time on that group
        long timeSpent = timeLogService.timeSpentOnGroupForFrame(condition.getTargetId(),
                TimeConverter.beginningOfOffsetDays(condition.getLastDaysCondition()),
                TimeConverter.endOfToday());
        return timeSpent >= condition.getUsageTimeCondition();
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
        Long hourNow = TimeConverter.epochToMilisOnGivenDay(now);
        Long from = lockDownStart();
        Long to = lockDownEnd();
        boolean isLockDown = (from < to && (from <= hourNow && hourNow <= to))
                || (from > to && (hourNow >= from || hourNow <= to));
        if (isLockDown) {
            Toaster.sendToast(localeMessages.getString("isLockDown"));
        } else if (closeToLock(hourNow, from)) {
            Toaster.sendToast(localeMessages.getString("closeToLock"));
        }
        return isLockDown;
    }

    private Long lockDownStart() {
        return Long.valueOf(configService.findById(ConfigurationEnum.LOCKDOWN_FROM).getValue());
    }

    private Long lockDownEnd() {
        return Long.valueOf(configService.findById(ConfigurationEnum.LOCKDOWN_TO).getValue());
    }

    private boolean closeToLock(Long hourNow, Long from) {
        Boolean isNotice = Boolean.valueOf(configService.findById(ConfigurationEnum.LOCK_NOTIFY).getValue());
        if (!isNotice) {
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
                .filter(s -> Pattern.compile("^\\d+$").matcher(s).matches()) // filter non numbers
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
    
    @Override
    public boolean isIdleWithToast() {
        boolean idle = isIdle();
        if (idle) {
            Toaster.sendToast(localeMessages.getString("idleMsg"));
        }
        return idle;
    }

}
