/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public interface WatchDog {
    public void begin(JTextArea logger);
    public void begin();
    public void setLogger(JTextArea logger);
    public void stop();
    public void addFeedbacker(FeedbackListener<String, TimeLog> feedbackListener);
    public void removeFeedbacker(FeedbackListener<String, TimeLog> feedbackListener);
}
