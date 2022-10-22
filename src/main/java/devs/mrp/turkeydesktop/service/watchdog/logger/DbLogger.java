/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logs.TimeLog;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public interface DbLogger {
    
    public void logEntry(long elapsed, String processPid, String processName, String windowTitle, Consumer<TimeLog> consumer);
    
}
