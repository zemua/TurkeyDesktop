/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logs.TimeLog;

/**
 *
 * @author miguel
 */
public interface DbLogger {
    
    public TimeLog logEntry(long elapsed, String processPid, String processName, String windowTitle);
    
}
