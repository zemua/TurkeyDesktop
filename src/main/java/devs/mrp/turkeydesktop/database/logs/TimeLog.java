/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.database.group.GroupFactory;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import io.reactivex.rxjava3.core.Single;
import lombok.EqualsAndHashCode;

/**
 *
 * @author miguel
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TimeLog {
    
    public static final String ID = "ID";
    public static final String EPOCH = "EPOCH";
    public static final String ELAPSED = "ELAPSED";
    public static final String COUNTED = "COUNTED";
    public static final String ACCUMULATED = "ACCUMULATED";
    public static final String PID = "PID";
    public static final String PROCESS_NAME = "PROCESS_NAME";
    public static final String WINDOW_TITLE = "WINDOW_TITLE";
    
    private long id;
    private long epoch;
    private long elapsed;
    private long counted;
    private long accumulated;
    private String pid;
    private String processName;
    private String windowTitle;
    private long groupId;
    private Type.Types type;
    
    private GroupService groupService = GroupFactory.getService();
    
    private boolean blockable;

    public boolean isBlockable() {
        return blockable;
    }

    public Single<TimeLog> setBlockable(boolean blockable) {
        // if we are asked to set as not blockable we just do it
        if (blockable == false) {
            this.blockable = blockable;
            return Single.just(this);
        }
        // if we are asked to set is as blockable we check if that is possible
        return groupService.isPreventClose(groupId).map(isPreventCloseResult -> {
            if (isPreventCloseResult) {
                this.blockable = false;
            } else {
                this.blockable = blockable;
            }
            return this;
        });
    }

    public Type.Types getType() {
        return type;
    }

    public void setType(Type.Types type) {
        this.type = type;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    private LocaleMessages localeMessages = LocaleMessages.getInstance();

    public long getAccumulated() {
        return accumulated;
    }

    public void setAccumulated(long accumulated) {
        this.accumulated = accumulated;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public long getCounted() {
        return counted;
    }

    public void setCounted(long counted) {
        this.counted = counted;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
    
    @Override
    public String toString() {
        try {
            return MessageFormat.format(localeMessages.getString("timeLogToString"),
                Optional.ofNullable(epoch).orElse(0L),
                Optional.ofNullable(elapsed).orElse(0L),
                Optional.ofNullable(processName).orElse(""),
                Optional.ofNullable(windowTitle).orElse(""));
        } catch (Exception e) {
            Logger.getLogger(TimeLog.class.getName()).log(Level.SEVERE, "Error formatting message", e);
            return "";
        }
    }
    
}
