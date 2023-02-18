package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class TimesHandler extends PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> {
    
    private final TimesPanelFactory factory;
    private final TimeLogService logService;
    private final LocaleMessages localeMessages = LocaleMessages.getInstance();
    private final TimeConverter timeConverter;
    
    public TimesHandler(JFrame frame, PanelHandler<?, ?, ?> caller, TimesPanelFactory factory) {
        super(frame, caller);
        this.logService = factory.getTimeLogService();
        this.factory = factory;
        this.timeConverter = factory.getTimeConverter();
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> initPanel() {
        this.setPanel(factory.getPanel());
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
            log.append(String.format(localeMessages.getString("processTimeLog"), t.getValue1(), timeConverter.millisToHMS(t.getValue2())));
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
