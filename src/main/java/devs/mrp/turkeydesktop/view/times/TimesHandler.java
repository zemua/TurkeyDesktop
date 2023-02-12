/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;

/**
 *
 * @author miguel
 */
public class TimesHandler extends PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> {

    private TimeLogService logService = TimeLogFactoryImpl.getService();
    
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
        updateLogs(getFrom(), getTo());
        /*logService.findLast24H(res -> {
            attachRecordsToLogger(res);
        });*/
    }
    
    private void initCaller() {
        exit();
    }
    
    private void updateLogs(Date from, Date to) {
        JTextArea log = (JTextArea)this.getPanel().getProperty(TimesEnum.LOGGER);
        log.setText("");
        logService.findProcessTimeFromTo(from, to).subscribe(t -> {
            log.append(String.format(localeMessages.getString("processTimeLog"), t.getValue1(), TimeConverter.millisToHMS(t.getValue2())));
        });
    }
    
    private Date getFrom() {
        return (Date)this.getPanel().getProperty(TimesEnum.FROM);
    }
    
    private Date getTo() {
        return (Date)this.getPanel().getProperty(TimesEnum.TO);
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
