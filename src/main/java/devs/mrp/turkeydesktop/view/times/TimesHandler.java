/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author miguel
 */
public class TimesHandler extends PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> {

    private ITimeLogService logService = FTimeLogService.getService();
    
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    
    public TimesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> initPanel() {
        this.setPanel(FTimesPanel.getPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    initCaller();
                    break;
                case UPDATE:
                    updateLogs(getFrom(), getTo());
                    break;
                default:
                    break;
            }
        });
    }
    
    @Override
    protected void doExtraBeforeShow() {
        attachRecordsToLogger(logService.findLast24H());
    }
    
    private void initCaller() {
        this.getCaller().show();
    }
    
    private void attachRecordsToLogger(List<TimeLog> list) {
        JTextArea log = (JTextArea)this.getPanel().getProperty(TimesEnum.LOGGER);
        list.forEach(e -> log.append(String.format("%s \n", e.toString())));
    }
    
    private void updateLogs(Date from, Date to) {
        JTextArea log = (JTextArea)this.getPanel().getProperty(TimesEnum.LOGGER);
        log.setText("");
        List<Dupla<String,Long>> times = logService.findProcessTimeFromTo(from, to);
        times.forEach(t -> log.append(String.format(localeMessages.getString("processTimeLog"), t.getValue1(), String.valueOf(t.getValue2()))));
    }
    
    private Date getFrom() {
        return (Date)this.getPanel().getProperty(TimesEnum.FROM);
    }
    
    private Date getTo() {
        return (Date)this.getPanel().getProperty(TimesEnum.TO);
    }
    
}
